package com.example.quizz.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.Toast;

import android.view.View;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.example.quizz.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageManager {
    private static ImageManager instance;
    private LruCache<String, Bitmap> memoryCache;
    private ExecutorService executorService;
    private final Map<ImageView, Future<?>> runningTasks = new ConcurrentHashMap<>();
    private final String BASE_URL = BuildConfig.BACKEND_HOST + "/images?key=";

    private ImageManager() {
        executorService = Executors.newFixedThreadPool(4);
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static synchronized ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    public interface ImageLoaderCallback {
        void onImageLoaded(Bitmap bitmap);
        void onError(Exception e);
        default void onStart() {}
        default void onTimeout() {}
    }

    public void displayImage(Context context, String category, String chapter, int num, ImageView imageView, View... viewsToDisable) {
        String imageName = num + "_small.jpg";
        String relativePath = category + "/Data/" + chapter + "/" + imageName;

        // Cancel previous task for this ImageView
        Future<?> oldTask = runningTasks.remove(imageView);
        if (oldTask != null) {
            oldTask.cancel(true);
        }

        getImage(context, relativePath, imageView, new ImageLoaderCallback() {
            @Override
            public void onStart() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    for (View v : viewsToDisable) {
                        if (v != null) v.setEnabled(false);
                    }
                    CircularProgressDrawable drawable = new CircularProgressDrawable(context);
                    drawable.setStrokeWidth(5f);
                    drawable.setCenterRadius(30f);
                    drawable.start();
                    imageView.setImageDrawable(drawable);
                });
            }

            @Override
            public void onImageLoaded(Bitmap bitmap) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    for (View v : viewsToDisable) {
                        if (v != null) v.setEnabled(true);
                    }
                    imageView.setImageBitmap(bitmap);
                });
            }

            @Override
            public void onTimeout() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    for (View v : viewsToDisable) {
                        if (v != null) v.setEnabled(true);
                    }
                    // Use a missing image placeholder
                    imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                    Toast.makeText(context, "Imaginea nu a putut fi descarcata, incercati mai tarziu", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onError(Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    for (View v : viewsToDisable) {
                        if (v != null) v.setEnabled(true);
                    }
                    e.printStackTrace();
                });
            }
        });
    }

    public void getImage(Context context, String relativePath, ImageView imageView, ImageLoaderCallback callback) {
        String cacheKey = relativePath;
        Bitmap cachedBitmap = memoryCache.get(cacheKey);
        if (cachedBitmap != null) {
            callback.onImageLoaded(cachedBitmap);
            return;
        }

        callback.onStart();

        Future<?> task = executorService.submit(() -> {
            HttpURLConnection connection = null;
            try {
                // Check disk cache
                File cacheDir = context.getCacheDir();
                String safeFileName = relativePath.replace("/", "_").replace(" ", "_");
                File localFile = new File(cacheDir, safeFileName);
                
                if (localFile.exists()) {
                    Bitmap diskBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    if (diskBitmap != null) {
                        memoryCache.put(cacheKey, diskBitmap);
                        callback.onImageLoaded(diskBitmap);
                        runningTasks.remove(imageView);
                        return;
                    }
                }

                // Fetch from network
                String encodedPath = relativePath.replace(" ", "%20");
                String urlString = BASE_URL + encodedPath;
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("x-api-key", BuildConfig.BACKEND_API_KEY);
                connection.setConnectTimeout(25000);
                connection.setReadTimeout(25000);
                connection.setDoInput(true);
                connection.connect();
                
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream input = connection.getInputStream();
                    Bitmap networkBitmap = BitmapFactory.decodeStream(input);

                    if (networkBitmap != null) {
                        // Save to disk cache
                        try (FileOutputStream out = new FileOutputStream(localFile)) {
                            networkBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        }
                        
                        memoryCache.put(cacheKey, networkBitmap);
                        callback.onImageLoaded(networkBitmap);
                    } else {
                        callback.onError(new Exception("Failed to decode bitmap"));
                    }
                } else {
                    callback.onError(new Exception("HTTP Error: " + responseCode));
                }
            } catch (java.net.SocketTimeoutException e) {
                callback.onTimeout();
            } catch (Exception e) {
                if (Thread.currentThread().isInterrupted()) {
                    // Task was cancelled
                } else {
                    callback.onError(e);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                runningTasks.remove(imageView);
            }
        });

        runningTasks.put(imageView, task);

        // Backup timeout handler in case connection timeouts don't catch everything (e.g. slow stream)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Future<?> currentTask = runningTasks.get(imageView);
            if (currentTask == task && !task.isDone()) {
                task.cancel(true);
                runningTasks.remove(imageView);
                callback.onTimeout();
            }
        }, 25000);
    }
}

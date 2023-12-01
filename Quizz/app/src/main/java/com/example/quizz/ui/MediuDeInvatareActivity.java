package com.example.quizz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.quizz.MainActivity;
import com.example.quizz.R;
import com.example.quizz.logic.Category;
import com.example.quizz.logic.Chapter;

import java.util.Map;
import java.util.Objects;

public class MediuDeInvatareActivity extends AppCompatActivity {

    void createButton(String chapterName, int index){
        String originalChapterName = "" + chapterName;
        chapterName = chapterName.replace(" ", "\n");
        Button button = new Button(this);
        button.setText(chapterName);

        // Set any additional properties for the button if needed

        // Create layout parameters to define how the button will be displayed
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                400,
                400
        );

        layoutParams.leftMargin = 50 + 500 * (index % 2);
        layoutParams.topMargin = 50 + 420 * (index / 2);
        // Add the button to the layout of the activity
        ViewGroup rootLayout = findViewById(R.id.form_layout);
        rootLayout.addView(button, layoutParams);

        button.setBackgroundColor(getResources().getColor(R.color.purple_500)); // Set background color
        button.setTextColor(getResources().getColor(android.R.color.white)); // Set text color


        // Set OnClickListener for the programmatically created button
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MediuDeInvatareActivity.this, AntrenamentActivity.class);
            intent.putExtra("chapter_name", originalChapterName);
            startActivity(intent);

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediu_de_invatare);

        AssetManager assetManager = this.getAssets();
        Category category = new Category(assetManager, "Categoria B");
        Map<String, Chapter> chapters = category.getChapterMap();
        int index = 0;
        for(Map.Entry<String, Chapter> entry : chapters.entrySet()) {
            createButton(entry.getKey(), index);
            index ++;
        }
    }
}
package com.example.quizz.logic;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Category {
    int numberCorrectRequired = 22;
    int numberQuestions = 26;
    int timeAllowed = 30;
    String category = "Categoria B";
    AssetManager assetManager;
    Map<String, Chapter> chapterMap;

    public int getNoQuestions(){
        return this.numberQuestions;
    }
    public Category(AssetManager assetManager, String category){
        this.assetManager = assetManager;

        try {
            String[] filelist = assetManager.list("Categoria B/Data");
            chapterMap = new HashMap<String, Chapter>();
            for(String fileName : filelist){
                if(!fileName.endsWith(".txt")){
                    continue;
                }
                String chapterName = fileName.substring(0, fileName.length() - 1 - 3);
                Chapter chapter = new Chapter(chapterName);

                InputStreamReader reader = new InputStreamReader(assetManager.open("Categoria B/Data/" + fileName));
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line = bufferedReader.readLine();
                int n = Integer.parseInt(line);

                for(int i = 0; i < n; i++){
                    String questionText = bufferedReader.readLine();
                    String answer1 = bufferedReader.readLine();
                    String answer2 = bufferedReader.readLine();
                    String answer3 = bufferedReader.readLine();
                    int correct = Integer.parseInt(bufferedReader.readLine());
                    boolean hasImage = bufferedReader.readLine().trim().equals("y");


                    Question q = new Question(questionText, answer1, answer2, answer3, correct, hasImage, i + 1, chapterName);
                    chapter.add(q);
                }

                chapterMap.put(chapterName, chapter);
            }

            InputStreamReader reader = new InputStreamReader(assetManager.open("Categoria B/Constraints.txt"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            int n = Integer.parseInt(line);

            for(int i = 0; i < n; i++){
                String chapterName = bufferedReader.readLine().trim();
                int select = Integer.parseInt(bufferedReader.readLine());
                chapterMap.get(chapterName).setSelect(select);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<String, Chapter> getChapterMap(){
        return this.chapterMap;
    }





}

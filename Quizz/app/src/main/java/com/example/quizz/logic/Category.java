package com.example.quizz.logic;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category {

    String categoryName = "";
    int numberCorrectRequired = 22;
    int numberQuestions = 26;
    int timeAllowed = 30;
    AssetManager assetManager;
    Map<String, Chapter> chapterMap;

    private ArrayList<Question> qList;
    private String chestionareFolder;

    public int getMaxWrong(){
        return this.numberQuestions - this.numberCorrectRequired;
    }

    public int getTimeAllowed(){
        return this.timeAllowed;
    }
    public int getNoQuestions(){
        return this.numberQuestions;
    }

    public String getCategoryName(){
        return this.categoryName;
    }

    public List<Question> getQList(){
        return this.qList;
    }
    public AssetManager getAssetManager(){
        return this.assetManager;
    }
    public Category(AssetManager assetManager, String category){
        qList = new ArrayList<>();
        this.assetManager = assetManager;
        this.categoryName = category + "";
        if(category.equals("Categoria A")){
            this.numberQuestions=20;
            this.numberCorrectRequired=17;
            this.timeAllowed = 15;
        } else if(category.equals("Category B")){

        }
        chestionareFolder = category + "/Data";
        try {
            int id = 0;
            String[] filelist = assetManager.list(category + "/Data");
            chapterMap = new HashMap<String, Chapter>();
            for(String fileName : filelist){
                if(!fileName.endsWith(".txt")){
                    continue;
                }
                String chapterName = fileName.substring(0, fileName.length() - 1 - 3);
                Chapter chapter = new Chapter(chapterName);

                InputStreamReader reader = new InputStreamReader(assetManager.open(category + "/Data/" + fileName));
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


                    Question q = new Question(questionText, answer1, answer2, answer3, correct, hasImage, i + 1, chapterName, id);
                    chapter.add(q);
                    this.qList.add(q);
                    id++;
                }

                chapterMap.put(chapterName, chapter);

            }

            InputStreamReader reader = new InputStreamReader(assetManager.open(category + "/Constraints.txt"));
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

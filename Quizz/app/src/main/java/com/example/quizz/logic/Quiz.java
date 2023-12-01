package com.example.quizz.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Quiz {


    public static List<Question> generateQuiz(Category c){
        List<Question> qList = new ArrayList<>();

        for (Map.Entry<String,Chapter> entry : c.getChapterMap().entrySet()){
            qList.addAll(entry.getValue().selectRandomQuestions());
        }

        return qList;
    }

    public static List<Question> generateQuiz(Category c, int chestionar){
        List<Question> qList = new ArrayList<>();

        try {
            String[] filelist = c.getAssetManager().list("Categoria B/Chestionare");
            for(String file : filelist){
                if(file.equals("chestionar " + chestionar + ".txt")){
                    InputStreamReader reader = new InputStreamReader(c.getAssetManager().open("Categoria B/Chestionare/" + file));
                    BufferedReader bufferedReader = new BufferedReader(reader);


                    String line = bufferedReader.readLine();
                    while(line != null){
                        String[] lines = line.split("=");
                        List<Question> qlist2 = c.getChapterMap().get(lines[0]).getQuestionList();
                        String[] numbs = lines[1].split(",");
                        for(int i = 0; i < numbs.length; i++){
                            qList.add(qlist2.get(Integer.parseInt(numbs[i])));
                        }
                        line = bufferedReader.readLine();
                    }
                }
            }
        } catch (Exception e){

        }

        return qList;
    }
}

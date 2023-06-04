package com.example.quizz.logic;

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
}

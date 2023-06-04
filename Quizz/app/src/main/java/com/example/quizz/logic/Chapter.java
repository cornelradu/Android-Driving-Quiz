package com.example.quizz.logic;

import android.text.Spannable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chapter {
    ArrayList<Question> questionList = null;
    String chapterName;
    int select = -1;
    public Chapter(String chapterName){
        this.chapterName = chapterName;
        this.questionList = new ArrayList<Question>();
    }

    public List<Question> getQuestionList(){
        return  questionList;
    }

    public void add(Question q){
        this.questionList.add(q);
    }

    public void setSelect(int select){
        this.select = select;
    }

    public int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public List<Question> selectRandomQuestions(){
        List<Integer> randomNumbers = new ArrayList<Integer>();
        int remainingNumbers = this.select;

        while(remainingNumbers != 0){
            int selected = 0;
            boolean found = false;

            do {
                selected = getRandomNumber(0, this.questionList.size()-1);
                found = false;
                for(int numb : randomNumbers){
                    if(numb == selected){
                        found = true;
                    }
                }
            } while (found);
            randomNumbers.add(selected);
            remainingNumbers -= 1;
        }

        List<Question> qList = new ArrayList<>();
        for(int numb : randomNumbers){
            qList.add(this.questionList.get(numb));
        }

        return qList;
    }
}

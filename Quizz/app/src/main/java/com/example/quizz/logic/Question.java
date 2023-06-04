package com.example.quizz.logic;

public class Question {
    String question;
    String answer1;
    String answer2;
    String answer3;
    int correctAnswer;
    boolean hasImage;

    int num;

    String chapterName;

    public Question(String question, String answer1, String answer2, String answer3, int correctAnswer, boolean hasImage, int num, String chapterName){
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.correctAnswer = correctAnswer;
        this.hasImage = hasImage;
        this.num = num;
        this.chapterName = chapterName;
    }

    public String getQuestion(){
        return this.question;
    }

    public  String getChapterName(){
        return this.chapterName;
    }

    public String[] getAnswers(){
        String[] ansL = new String[]{this.answer1, this.answer2, this.answer3};
        return ansL;
    }

    public int getNum(){
        return this.num;
    }

    public boolean hasImage(){
        return this.hasImage;
    }

    public int getCorrectAnswer(){
        return this.correctAnswer;
    }
}

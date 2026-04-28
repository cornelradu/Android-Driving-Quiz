package com.dmradu.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmradu.quiz.R;
import com.dmradu.quiz.logic.Category;
import com.dmradu.quiz.logic.Chapter;
import com.dmradu.quiz.logic.Question;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class ExamineazaRaspunsuriGresite extends AppCompatActivity {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Question> questionList;

    int index = 0;

    String[] answered;

    int noAnswered = 0;
    int correctAnswered = 0;

    int time = 0;

    boolean answer1Set = false;
    boolean answer2Set = false;
    boolean answer3Set = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examineaza_raspunsuri_gresite);
        AssetManager assetManager = this.getAssets();
        Category c = new Category(assetManager, this.getIntent().getStringExtra("categoria"));
        int w = getIntent().getIntExtra("wrong", 0);
        int[] questionIds = getIntent().getIntArrayExtra("questionIds");
        String[] answered = new String[w];
        int[] answeredCodes = new int[w];
        this.questionList = new ArrayList<>();

        int index2 = 0;
        for(int i = 0; i < c.getNoQuestions(); i++){
            if(getIntent().getStringArrayExtra("answered")[i].equals("Wrong")){
                this.questionList.add(c.getQList().get(questionIds[i]));
                answered[index2] = getIntent().getStringArrayExtra("answered")[i];
                answeredCodes[index2] = getIntent().getIntArrayExtra("answeredCodes")[i];
                index2++;
            }
        }


        this.answered = new String[this.questionList.size()];

        final TextView correctTextView = (TextView)findViewById(R.id.correct_answer_num);
        String correct = "";
        int answ = this.questionList.get(0).getCorrectAnswer();
        answ -= 1000;
        if(answ / 100 >= 1){
            correct += "A,";
            answ -= 100;
        }

        if(answ / 10 >= 1){
            correct += "B,";
            answ -= 10;
        }

        if(answ == 1){
            correct += "C,";
        }
        correctTextView.setText(correct.substring(0, correct.length()-1));

        String wrong = "";
        answ = answeredCodes[0];
        answ -= 1000;
        if(answ / 100 >= 1){
            wrong += "A,";
            answ -= 100;
        }

        if(answ / 10 >= 1){
            wrong += "B,";
            answ -= 10;
        }

        if(answ == 1){
            wrong += "C,";
        }
        final TextView wrongTextView = (TextView)findViewById(R.id.wrong_answer_num);
        wrongTextView.setText(wrong.substring(0, wrong.length()-1));


        final TextView questionTextView = (TextView)findViewById(R.id.question);
        questionTextView.setText(this.questionList.get(0).getQuestion());

        final TextView answer1TextView = (TextView)findViewById(R.id.answer1);
        answer1TextView.setText(this.questionList.get(0).getAnswers()[0]);

        final TextView answer2TextView = (TextView)findViewById(R.id.answer2);
        answer2TextView.setText(this.questionList.get(0).getAnswers()[1]);

        final TextView answer3TextView = (TextView)findViewById(R.id.answer3);
        answer3TextView.setText(this.questionList.get(0).getAnswers()[2]);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button nextQuestion = (Button ) findViewById(R.id.nextQuestion);
        if(questionList.get(0).hasImage()) {
            com.dmradu.quiz.logic.ImageManager.getInstance().displayImage(
                    this,
                    this.getIntent().getStringExtra("categoria"),
                    questionList.get(0).getChapterName(),
                    questionList.get(0).getNum(),
                    imageView,
                    nextQuestion
            );
            imageView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
            lp.height = 600;
        }else {
            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
            lp.height = 0;
            imageView.setLayoutParams(lp);
            imageView.setVisibility(View.INVISIBLE);
        }

        AppCompatActivity that = this;
        final List<Question> qList = this.questionList;
        Button reiaChestionarul = (Button) findViewById(R.id.reiaChestionarul);


        Button chestionarNou = (Button) findViewById(R.id.chestionarNou);

        reiaChestionarul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(2, i);
                ExamineazaRaspunsuriGresite.super.onBackPressed();
            }
        });

        chestionarNou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                setResult(3, i);
                ExamineazaRaspunsuriGresite.super.onBackPressed();
            }
        });

        nextQuestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        index += 1;
                        if(index == qList.size()){
                            index = 0;
                        }

                        String correct = "";
                        int answ = qList.get(index).getCorrectAnswer();
                        answ -= 1000;
                        if(answ / 100 >= 1){
                            correct += "A,";
                            answ -= 100;
                        }

                        if(answ / 10 >= 1){
                            correct += "B,";
                            answ -= 10;
                        }

                        if(answ == 1){
                            correct += "C,";
                        }
                        correctTextView.setText(correct.substring(0, correct.length()-1));

                        String wrong = "";
                        answ = answeredCodes[index];
                        answ -= 1000;
                        if(answ / 100 >= 1){
                            wrong += "A, ";
                            answ -= 100;
                        }

                        if(answ / 10 >= 1){
                            wrong += "B, ";
                            answ -= 10;
                        }

                        if(answ == 1){
                            wrong += "C,";
                        }
                        final TextView wrongTextView = (TextView)findViewById(R.id.wrong_answer_num);
                        wrongTextView.setText(wrong.substring(0, wrong.length()-1));



                        questionTextView.setText((index+1) + ". " + qList.get(index).getQuestion());

                        answer1TextView.setText(qList.get(index).getAnswers()[0]);

                        answer2TextView.setText(qList.get(index).getAnswers()[1]);

                        answer3TextView.setText(qList.get(index).getAnswers()[2]);



                        if(qList.get(index).hasImage()) {
                            com.dmradu.quiz.logic.ImageManager.getInstance().displayImage(
                                    ExamineazaRaspunsuriGresite.this,
                                    that.getIntent().getStringExtra("categoria"),
                                    qList.get(index).getChapterName(),
                                    qList.get(index).getNum(),
                                    imageView,
                                    nextQuestion
                            );
                            imageView.setVisibility(View.VISIBLE);
                            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                            lp.height = 600;
                            imageView.setLayoutParams(lp);
                        } else {
                            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                            lp.height = 0;
                            imageView.setLayoutParams(lp);
                            imageView.setVisibility(View.INVISIBLE);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                       return true;


                }
                return true;
            }

        });

        initAds();
    }

    private void initAds() {
        Executors.newSingleThreadExecutor().execute(() -> {
            MobileAds.initialize(this, initializationStatus -> {});
            runOnUiThread(() -> {
                AdView adView = findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            });
        });
    }
}
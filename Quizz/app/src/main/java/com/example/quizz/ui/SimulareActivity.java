package com.example.quizz.ui;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizz.R;
import com.example.quizz.logic.Category;
import com.example.quizz.logic.Question;
import com.example.quizz.logic.Quiz;

import java.util.List;

public class SimulareActivity extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int index = 0;

    List<Question> questionList;

    String[] answered;

    int maxWrong = 4;
    int noAnswered = 0;
    int correctAnswered = 0;

    int time = 0;

    boolean answer1Set = false;
    boolean answer2Set = false;
    boolean answer3Set = false;

    Category category = null;


    //finish(null, noAnswered - correctAnswered, correctAnswered, "Failed");
    private void finish(Object object, int wrong, int correct, String result){
        Intent intent = new Intent(SimulareActivity.this, ResultInfoActivity.class);
        intent.putExtra("wrong", wrong);
        intent.putExtra("correct", correct);
        intent.putExtra("result", result);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulare);

        AssetManager assetManager = this.getAssets();

        this.category = new Category(assetManager, "Categoria B");
        final int noOfQ = this.category.getNoQuestions();
        int chestionar = getIntent().getIntExtra("chestionar", 0);
        if (chestionar == 0){
            this.questionList = Quiz.generateQuiz(this.category);
        } else {
            this.questionList = Quiz.generateQuiz(this.category, chestionar);
        }
        this.answered = new String[this.category.getNoQuestions()];
        for(int i = 0; i < this.answered.length; i++){
            this.answered[i] = "Not Answered";
        }

        time = 30 * 60;

        final TextView questionTextView = (TextView)findViewById(R.id.question);
        questionTextView.setText(this.questionList.get(0).getQuestion());

        final TextView answer1TextView = (TextView)findViewById(R.id.answer1);
        answer1TextView.setText(this.questionList.get(0).getAnswers()[0]);

        final TextView answer2TextView = (TextView)findViewById(R.id.answer2);
        answer2TextView.setText(this.questionList.get(0).getAnswers()[1]);

        final TextView answer3TextView = (TextView)findViewById(R.id.answer3);
        answer3TextView.setText(this.questionList.get(0).getAnswers()[2]);

        ImageButton nextQuestion = (ImageButton) findViewById(R.id.nextQuestion);
        final String[] ans = this.answered;

        final TextView initialQuestions = (TextView)findViewById(R.id.initial_questions);
        initialQuestions.setText(noOfQ + " Intrebari initiale");

        final TextView remainingQuestionsView = (TextView)findViewById(R.id.remaining_questions);
        remainingQuestionsView.setText(noOfQ + " Intrebari ramase");

        final TextView correctAnswersView = (TextView)findViewById(R.id.correct_answers);

        final TextView wrongAnswersView = (TextView)findViewById(R.id.wrong_answers);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        if(questionList.get(0).hasImage()) {
            try {
                imageView.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Categoria B/Data/" + questionList.get(0).getChapterName() + "/" + questionList.get(0).getNum() + ".jpg")));

            } catch (Exception e) {
                e.printStackTrace();
            }
            imageView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
            lp.height = 600;
        }else {
            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
            lp.height = 0;
            imageView.setLayoutParams(lp);
            imageView.setVisibility(View.INVISIBLE);
        }

        final Button buttonA = (Button) findViewById(R.id.buttonA);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer1Set = !answer1Set;
                if(answer1Set){
                    buttonA.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else {
                    buttonA.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }
            }
        });

        final Button buttonB = (Button) findViewById(R.id.buttonB);
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer2Set = !answer2Set;
                if(answer2Set){
                    buttonB.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else {
                    buttonB.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }
            }
        });

        final Button buttonC = (Button) findViewById(R.id.buttonC);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer1Set = !answer1Set;
                if(answer1Set){
                    buttonC.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else {
                    buttonC.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }
            }
        });

        final List<Question> qList = this.questionList;
        nextQuestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        nextQuestion.setBackgroundColor(getResources().getColor(R.color.yellow));
                        do{
                            index += 1;
                            if(index == noOfQ){
                                index = 0;
                            }
                        } while (!ans[index].equals("Not Answered"));

                        questionTextView.setText(qList.get(index).getQuestion());

                        answer1TextView.setText(qList.get(index).getAnswers()[0]);

                        answer2TextView.setText(qList.get(index).getAnswers()[1]);

                        answer3TextView.setText(qList.get(index).getAnswers()[2]);

                        buttonA.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonB.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonC.setBackgroundColor(getResources().getColor(R.color.light_blue));

                        answer1Set = false;
                        answer2Set = false;
                        answer3Set = false;

                        if(questionList.get(index).hasImage()) {
                            try {
                                imageView.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Categoria B/Data/" + questionList.get(index).getChapterName() + "/" + questionList.get(index).getNum() + ".jpg")));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            imageView.setVisibility(View.VISIBLE);
                            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                            lp.height = 600;
                        } else {
                            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                            lp.height = 0;
                            imageView.setLayoutParams(lp);
                            imageView.setVisibility(View.INVISIBLE);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        nextQuestion.setBackgroundColor(Color.TRANSPARENT);


                }
                return true;
            }

        });

        final ImageButton buttonDeleteAnswer = (ImageButton) findViewById(R.id.deleteAnswer);
        buttonDeleteAnswer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        buttonDeleteAnswer.setBackgroundColor(getResources().getColor(R.color.yellow));
                        buttonA.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonB.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonC.setBackgroundColor(getResources().getColor(R.color.light_blue));

                        answer1Set = false;
                        answer2Set = false;
                        answer3Set = false;
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        buttonDeleteAnswer.setBackgroundColor(Color.TRANSPARENT);


                }
                return true;
            }

        });

        final ImageButton sendAnswer = (ImageButton) findViewById(R.id.sendAnswer);
        sendAnswer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        sendAnswer.setBackgroundColor(getResources().getColor(R.color.yellow));
                        int expectedAnswer = qList.get(index).getCorrectAnswer();

                        //sendAnswer.setBackgroundColor(getResources().getColor(R.color.yellow));

                        int sum = 1000;
                        if(answer1Set){
                            sum += 100;
                        }
                        if(answer2Set){
                            sum += 10;
                        }
                        if(answer3Set){
                            sum += 1;
                        }

                        if(expectedAnswer == sum){
                            answered[index] = "Correct";
                            correctAnswered += 1;
                        } else {
                            answered[index] = "Wrong";
                        }
                        noAnswered += 1;

                        remainingQuestionsView.setText((noOfQ - noAnswered) + " Intrebari ramase");

                        correctAnswersView.setText((correctAnswered) + " Raspunsuri corecte");

                        wrongAnswersView.setText((noAnswered-correctAnswered) + " Raspunsuri gresite");

                        if(noAnswered - correctAnswered > maxWrong){
                            finish(null, noAnswered - correctAnswered, correctAnswered, "Failed");
                            return true;
                        } else if(noAnswered == noOfQ){
                            finish(null, noAnswered - correctAnswered, correctAnswered, "Succeded");
                        }

                        do{
                            index += 1;
                            if(index == noOfQ){
                                index = 0;
                            }
                        } while (!ans[index].equals("Not Answered"));

                        questionTextView.setText(qList.get(index).getQuestion());

                        answer1TextView.setText(qList.get(index).getAnswers()[0]);

                        answer2TextView.setText(qList.get(index).getAnswers()[1]);

                        answer3TextView.setText(qList.get(index).getAnswers()[2]);

                        buttonA.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonB.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonC.setBackgroundColor(getResources().getColor(R.color.light_blue));

                        answer1Set = false;
                        answer2Set = false;
                        answer3Set = false;
                        if(questionList.get(index).hasImage()) {
                            try {
                                imageView.setImageBitmap(BitmapFactory.decodeStream(assetManager.open("Categoria B/Data/" + questionList.get(index).getChapterName() + "/" + questionList.get(index).getNum() + ".jpg")));

                            } catch (Exception e) {

                            }
                            imageView.setVisibility(View.VISIBLE);
                            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                            lp.height = 600;
                        }else {
                            ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) imageView.getLayoutParams();
                            lp.height = 0;
                            imageView.setLayoutParams(lp);
                            imageView.setVisibility(View.INVISIBLE);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        sendAnswer.setBackgroundColor(Color.TRANSPARENT);


                }
                return true;
            }

        });

        Handler handler = new Handler();
        final TextView timeRemainingVIew = (TextView)findViewById(R.id.remaining_time);
        final Runnable r = new Runnable() {
            public void run() {
                time -= 1;
                timeRemainingVIew.setText(time/3600 + ":" + (time/60) + ":" + (time%60) + " Timp ramas");
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
           super.onBackPressed();
    }

}
package com.example.quizz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizz.R;
import com.example.quizz.logic.Category;
import com.example.quizz.logic.Question;
import com.example.quizz.logic.Quiz;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulareActivity extends AppCompatActivity {

    private int index = 0;
    private List<Question> questionList;
    private String[] answered;
    private int[] answeredCodes;
    private int maxWrong = 4;
    private int noAnswered = 0;
    private int correctAnswered = 0;
    private int time = 0;

    private boolean answer1Set = false;
    private boolean answer2Set = false;
    private boolean answer3Set = false;

    private Category category = null;
    private int chestionar = 0;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private void finishQuiz(int wrong, int correct, String result) {
        Intent intent = new Intent(SimulareActivity.this, ResultInfoActivity.class);
        intent.putExtra("wrong", wrong);
        intent.putExtra("correct", correct);
        intent.putExtra("result", result);
        intent.putExtra("answered", answered);

        int[] questionIds = new int[this.category.getNoQuestions()];
        for (int i = 0; i < questionIds.length; i++) {
            questionIds[i] = this.questionList.get(i).getId();
        }
        intent.putExtra("questionIds", questionIds);
        intent.putExtra("categoria", this.category.getCategoryName());
        intent.putExtra("answeredCodes", this.answeredCodes);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulare);

        // Defer heavy data loading to background thread
        executorService.execute(() -> {
            AssetManager assetManager = getAssets();
            String categoriaName = getIntent().getStringExtra("categoria");
            this.category = new Category(assetManager, categoriaName);
            this.maxWrong = this.category.getMaxWrong();
            this.chestionar = getIntent().getIntExtra("chestionar", 0);

            if (chestionar == 0) {
                this.questionList = Quiz.generateQuiz(this.category);
            } else {
                this.questionList = Quiz.generateQuiz(this.category, chestionar);
            }

            int noOfQ = this.category.getNoQuestions();
            this.answered = new String[noOfQ];
            this.answeredCodes = new int[noOfQ];
            for (int i = 0; i < noOfQ; i++) {
                this.answered[i] = "Not Answered";
            }
            this.time = category.getTimeAllowed() * 60;

            // Update UI on main thread
            new Handler(Looper.getMainLooper()).post(this::initializeUI);
        });

        // Async AdMob initialization
        MobileAds.initialize(this, status -> {
            com.google.android.gms.ads.AdView mAdView = findViewById(R.id.adView);
            if (mAdView != null) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeUI() {
        if (questionList == null || questionList.isEmpty()) return;

        final TextView questionTextView = findViewById(R.id.question);
        final TextView answer1TextView = findViewById(R.id.answer1);
        final TextView answer2TextView = findViewById(R.id.answer2);
        final TextView answer3TextView = findViewById(R.id.answer3);
        final TextView initialQuestions = findViewById(R.id.initial_questions_num);
        final TextView remainingQuestionsView = findViewById(R.id.remaining_questions_num);
        final TextView correctAnswersView = findViewById(R.id.correct_answers_num);
        final TextView wrongAnswersView = findViewById(R.id.wrong_answers_num);
        final TextView timeRemainingView = findViewById(R.id.remaining_time);
        
        final Button nextQuestion = findViewById(R.id.nextQuestion);
        final Button sendAnswer = findViewById(R.id.sendAnswer);
        final Button buttonDeleteAnswer = findViewById(R.id.deleteAnswer);
        final ImageView imageView = findViewById(R.id.imageView);
        
        final Button buttonA = findViewById(R.id.buttonA);
        final Button buttonB = findViewById(R.id.buttonB);
        final Button buttonC = findViewById(R.id.buttonC);

        int noOfQ = category.getNoQuestions();
        initialQuestions.setText(String.valueOf(noOfQ));
        remainingQuestionsView.setText(String.valueOf(noOfQ));
        updateQuestionUI(questionTextView, answer1TextView, answer2TextView, answer3TextView, imageView, nextQuestion, sendAnswer);

        // Selection Logic
        View.OnClickListener answerClick = v -> {
            if (v == buttonA || v == answer1TextView) answer1Set = !answer1Set;
            else if (v == buttonB || v == answer2TextView) answer2Set = !answer2Set;
            else if (v == buttonC || v == answer3TextView) answer3Set = !answer3Set;
            updateSelectionColors(buttonA, buttonB, buttonC, answer1TextView, answer2TextView, answer3TextView);
        };

        buttonA.setOnClickListener(answerClick);
        buttonB.setOnClickListener(answerClick);
        buttonC.setOnClickListener(answerClick);
        answer1TextView.setOnClickListener(answerClick);
        answer2TextView.setOnClickListener(answerClick);
        answer3TextView.setOnClickListener(answerClick);

        // Navigation
        nextQuestion.setOnClickListener(v -> {
            moveToNextUnanswered();
            resetSelection(buttonA, buttonB, buttonC, answer1TextView, answer2TextView, answer3TextView);
            updateQuestionUI(questionTextView, answer1TextView, answer2TextView, answer3TextView, imageView, nextQuestion, sendAnswer);
        });

        buttonDeleteAnswer.setOnClickListener(v -> {
            resetSelection(buttonA, buttonB, buttonC, answer1TextView, answer2TextView, answer3TextView);
        });

        sendAnswer.setOnClickListener(v -> {
            processAnswer(remainingQuestionsView, correctAnswersView, wrongAnswersView);

            if (noAnswered - correctAnswered > maxWrong) {
                finishQuiz(noAnswered - correctAnswered, correctAnswered, "Failed");
            } else if (noAnswered == category.getNoQuestions()) {
                finishQuiz(noAnswered - correctAnswered, correctAnswered, "Succeeded");
            } else {
                moveToNextUnanswered();
                resetSelection(buttonA, buttonB, buttonC, answer1TextView, answer2TextView, answer3TextView);
                updateQuestionUI(questionTextView, answer1TextView, answer2TextView, answer3TextView, imageView, nextQuestion, sendAnswer);
            }
        });

        // Timer
        Handler timerHandler = new Handler();
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (time > 0) {
                    time--;
                    int h = time / 3600;
                    int m = (time % 3600) / 60;
                    int s = time % 60;
                    timeRemainingView.setText(String.format("%02d:%02d:%02d", h, m, s));
                    timerHandler.postDelayed(this, 1000);
                } else {
                    finishQuiz(noAnswered - correctAnswered, correctAnswered, "Failed - Time Expired");
                }
            }
        }, 1000);
    }

    private void updateQuestionUI(TextView qTv, TextView a1, TextView a2, TextView a3, ImageView iv, View next, View send) {
        Question q = questionList.get(index);
        qTv.setText(q.getQuestion());
        a1.setText(q.getAnswers()[0]);
        a2.setText(q.getAnswers()[1]);
        a3.setText(q.getAnswers()[2]);

        if (q.hasImage()) {
            iv.setVisibility(View.VISIBLE);
            com.example.quizz.logic.ImageManager.getInstance().displayImage(
                    this, category.getCategoryName(), q.getChapterName(), q.getNum(), iv, next, send
            );
            ViewGroup.LayoutParams lp = iv.getLayoutParams();
            lp.height = 600;
            iv.setLayoutParams(lp);
        } else {
            iv.setVisibility(View.GONE);
            ViewGroup.LayoutParams lp = iv.getLayoutParams();
            lp.height = 0;
            iv.setLayoutParams(lp);
        }
    }

    private void updateSelectionColors(Button bA, Button bB, Button bC, TextView t1, TextView t2, TextView t3) {
        int yellow = getResources().getColor(R.color.yellow);
        int lightBlue = getResources().getColor(R.color.light_blue);

        bA.setBackgroundTintList(android.content.res.ColorStateList.valueOf(answer1Set ? yellow : lightBlue));
        bB.setBackgroundTintList(android.content.res.ColorStateList.valueOf(answer2Set ? yellow : lightBlue));
        bC.setBackgroundTintList(android.content.res.ColorStateList.valueOf(answer3Set ? yellow : lightBlue));

        t1.setBackgroundColor(answer1Set ? Color.YELLOW : Color.TRANSPARENT);
        t2.setBackgroundColor(answer2Set ? Color.YELLOW : Color.TRANSPARENT);
        t3.setBackgroundColor(answer3Set ? Color.YELLOW : Color.TRANSPARENT);
    }

    private void resetSelection(Button bA, Button bB, Button bC, TextView t1, TextView t2, TextView t3) {
        answer1Set = answer2Set = answer3Set = false;
        updateSelectionColors(bA, bB, bC, t1, t2, t3);
    }

    private void moveToNextUnanswered() {
        int start = index;
        do {
            index = (index + 1) % category.getNoQuestions();
        } while (!answered[index].equals("Not Answered") && index != start);
    }

    private void processAnswer(TextView rem, TextView corr, TextView wrong) {
        int expected = questionList.get(index).getCorrectAnswer();
        int sum = 1000 + (answer1Set ? 100 : 0) + (answer2Set ? 10 : 0) + (answer3Set ? 1 : 0);
        answeredCodes[index] = sum;
        if (expected == sum) {
            answered[index] = "Correct";
            correctAnswered++;
        } else {
            answered[index] = "Wrong";
        }
        noAnswered++;
        rem.setText(String.valueOf(category.getNoQuestions() - noAnswered));
        corr.setText(String.valueOf(correctAnswered));
        wrong.setText(String.valueOf(noAnswered - correctAnswered));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 2 && resultCode != 3) {
            super.onBackPressed();
        } else {
            recreate(); // Simple way to restart the quiz session
        }
    }
}
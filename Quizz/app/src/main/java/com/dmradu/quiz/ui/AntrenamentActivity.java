package com.dmradu.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmradu.quiz.R;
import com.dmradu.quiz.logic.Category;
import com.dmradu.quiz.logic.Chapter;
import com.dmradu.quiz.logic.Question;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class AntrenamentActivity extends AppCompatActivity {
    private List<Question> questionList;
    private int index = 0;
    private String[] answered;
    private int noAnswered = 0;
    private int correctAnswered = 0;

    private boolean answer1Set = false;
    private boolean answer2Set = false;
    private boolean answer3Set = false;

    private String categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antrenament);

        AssetManager assetManager = this.getAssets();
        categoria = getIntent().getStringExtra("categoria");
        Category c = new Category(assetManager, categoria);
        Map<String, Chapter> map = c.getChapterMap();
        questionList = map.get(getIntent().getStringExtra("chapter_name")).getQuestionList();

        this.answered = new String[this.questionList.size()];
        for (int i = 0; i < this.answered.length; i++) {
            this.answered[i] = "Not Answered";
        }

        initializeUI();
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

    @SuppressLint("ClickableViewAccessibility")
    private void initializeUI() {
        final TextView questionTextView = findViewById(R.id.question);
        final TextView answer1TextView = findViewById(R.id.answer1);
        final TextView answer2TextView = findViewById(R.id.answer2);
        final TextView answer3TextView = findViewById(R.id.answer3);
        final TextView initialQuestions = findViewById(R.id.initial_questions_num);
        final TextView remainingQuestionsView = findViewById(R.id.remaining_questions_num);
        final TextView correctAnswersView = findViewById(R.id.correct_answers_num);
        final TextView wrongAnswersView = findViewById(R.id.wrong_answers_num);
        final ImageView imageView = findViewById(R.id.imageView);
        final EditText editTextNumber = findViewById(R.id.editTextNumber);

        final Button buttonA = findViewById(R.id.buttonA);
        final Button buttonB = findViewById(R.id.buttonB);
        final Button buttonC = findViewById(R.id.buttonC);
        final Button nextQuestion = findViewById(R.id.nextQuestion);
        final Button sendAnswer = findViewById(R.id.sendAnswer);
        final Button buttonDeleteAnswer = findViewById(R.id.deleteAnswer);

        initialQuestions.setText(String.valueOf(questionList.size()));
        remainingQuestionsView.setText(String.valueOf(questionList.size()));

        updateQuestionUI(questionTextView, answer1TextView, answer2TextView, answer3TextView, imageView, nextQuestion, sendAnswer);

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
            moveToNextUnanswered();
            resetSelection(buttonA, buttonB, buttonC, answer1TextView, answer2TextView, answer3TextView);
            updateQuestionUI(questionTextView, answer1TextView, answer2TextView, answer3TextView, imageView, nextQuestion, sendAnswer);
        });

        editTextNumber.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int requestedIndex = Integer.parseInt(s.toString()) - 1;
                    if (requestedIndex >= 0 && requestedIndex < questionList.size()) {
                        index = requestedIndex;
                        resetSelection(buttonA, buttonB, buttonC, answer1TextView, answer2TextView, answer3TextView);
                        updateQuestionUI(questionTextView, answer1TextView, answer2TextView, answer3TextView, imageView, nextQuestion, sendAnswer);
                    }
                } catch (Exception ignored) {}
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void updateQuestionUI(TextView qTv, TextView a1, TextView a2, TextView a3, ImageView iv, View next, View send) {
        Question q = questionList.get(index);
        qTv.setText((index + 1) + ". " + q.getQuestion());
        a1.setText(q.getAnswers()[0]);
        a2.setText(q.getAnswers()[1]);
        a3.setText(q.getAnswers()[2]);

        if (q.hasImage()) {
            iv.setVisibility(View.VISIBLE);
            com.dmradu.quiz.logic.ImageManager.getInstance().displayImage(
                    this, categoria, q.getChapterName(), q.getNum(), iv, next, send
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
        int yellow = androidx.core.content.ContextCompat.getColor(this, R.color.yellow);
        int lightBlue = androidx.core.content.ContextCompat.getColor(this, R.color.light_blue);

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
            index = (index + 1) % questionList.size();
        } while (!answered[index].equals("Not Answered") && index != start);
    }

    private void processAnswer(TextView rem, TextView corr, TextView wrong) {
        int expected = questionList.get(index).getCorrectAnswer();
        int sum = 1000 + (answer1Set ? 100 : 0) + (answer2Set ? 10 : 0) + (answer3Set ? 1 : 0);
        
        if (expected == sum) {
            answered[index] = "Correct";
            correctAnswered++;
        } else {
            answered[index] = "Wrong";
        }
        noAnswered++;
        rem.setText(String.valueOf(questionList.size() - noAnswered));
        corr.setText(String.valueOf(correctAnswered));
        wrong.setText(String.valueOf(noAnswered - correctAnswered));
    }
}

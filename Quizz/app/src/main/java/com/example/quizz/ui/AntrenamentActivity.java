package com.example.quizz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.quizz.R;
import com.example.quizz.logic.Category;
import com.example.quizz.logic.Chapter;
import com.example.quizz.logic.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AntrenamentActivity extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
        setContentView(R.layout.activity_antrenament);
        AssetManager assetManager = this.getAssets();
        Category c = new Category(assetManager, this.getIntent().getStringExtra("categoria"));

        Map<String, Chapter> map = c.getChapterMap();


        setContentView(R.layout.activity_antrenament);

        questionList = map.get(getIntent().getStringExtra("chapter_name")).getQuestionList();


        this.answered = new String[this.questionList.size()];
        for(int i = 0; i < this.answered.length; i++){
            this.answered[i] = "Not Answered";
        }


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

        final TextView initialQuestions = (TextView)findViewById(R.id.initial_questions_num);
        initialQuestions.setText(questionList.size() + "");

        final TextView remainingQuestionsView = (TextView)findViewById(R.id.remaining_questions_num);
        remainingQuestionsView.setText(questionList.size() + "");

        final TextView correctAnswersView = (TextView)findViewById(R.id.correct_answers_num);

        final TextView wrongAnswersView = (TextView)findViewById(R.id.wrong_answers_num);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        if(questionList.get(0).hasImage()) {
            try {
                imageView.setImageBitmap(BitmapFactory.decodeStream(assetManager.open( this.getIntent().getStringExtra("categoria")+ "/Data/" + questionList.get(0).getChapterName() + "/" + questionList.get(0).getNum() + ".jpg")));

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

        AppCompatActivity that = this;
        final List<Question> qList = this.questionList;
        nextQuestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        nextQuestion.setBackgroundColor(getResources().getColor(R.color.yellow));
                        do{
                            index += 1;
                            if(index == questionList.size()){
                                index = 0;
                            }
                        } while (!ans[index].equals("Not Answered"));

                        questionTextView.setText((index+1) + ". " + qList.get(index).getQuestion());

                        answer1TextView.setText(qList.get(index).getAnswers()[0]);

                        answer2TextView.setText(qList.get(index).getAnswers()[1]);

                        answer3TextView.setText(qList.get(index).getAnswers()[2]);

                        buttonA.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonB.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonC.setBackgroundColor(getResources().getColor(R.color.light_blue));

                        answer1Set = false;
                        answer2Set = false;
                        answer3Set = false;
                        if(answer1Set){
                            answer1TextView.setBackgroundColor(Color.YELLOW);
                        } else {
                            answer1TextView.setBackgroundColor(Color.TRANSPARENT);
                        }

                        if(answer2Set){
                            answer2TextView.setBackgroundColor(Color.YELLOW);
                        } else {
                            answer2TextView.setBackgroundColor(Color.TRANSPARENT);
                        }


                        if(answer3Set){
                            answer3TextView.setBackgroundColor(Color.YELLOW);
                        } else {
                            answer3TextView.setBackgroundColor(Color.TRANSPARENT);
                        }

                        if(questionList.get(index).hasImage()) {
                            try {
                                imageView.setImageBitmap(BitmapFactory.decodeStream(assetManager.open( that.getIntent().getStringExtra("categoria") + "/Data/" + questionList.get(index).getChapterName() + "/" + questionList.get(index).getNum() + ".jpg")));

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
                        if(answer1Set){
                            answer1TextView.setBackgroundColor(Color.YELLOW);
                        } else {
                            answer1TextView.setBackgroundColor(Color.TRANSPARENT);
                        }

                        if(answer2Set){
                            answer2TextView.setBackgroundColor(Color.YELLOW);
                        } else {
                            answer2TextView.setBackgroundColor(Color.TRANSPARENT);
                        }


                        if(answer3Set){
                            answer3TextView.setBackgroundColor(Color.YELLOW);
                        } else {
                            answer3TextView.setBackgroundColor(Color.TRANSPARENT);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        buttonDeleteAnswer.setBackgroundColor(Color.TRANSPARENT);


                }
                return true;
            }

        });

        final ImageButton sendAnswer = (ImageButton)findViewById(R.id.sendAnswer);
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

                        remainingQuestionsView.setText((questionList.size() - noAnswered) + "");

                        correctAnswersView.setText((correctAnswered) + "");

                        wrongAnswersView.setText((noAnswered-correctAnswered) + "");


                        do{
                            index += 1;
                            if(index == questionList.size()){
                                index = 0;
                            }
                        } while (!ans[index].equals("Not Answered"));

                        questionTextView.setText((index+1) + ". " + questionList.get(index).getQuestion());

                        answer1TextView.setText(questionList.get(index).getAnswers()[0]);

                        answer2TextView.setText(questionList.get(index).getAnswers()[1]);

                        answer3TextView.setText(questionList.get(index).getAnswers()[2]);

                        buttonA.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonB.setBackgroundColor(getResources().getColor(R.color.light_blue));
                        buttonC.setBackgroundColor(getResources().getColor(R.color.light_blue));

                        answer1Set = false;
                        answer2Set = false;
                        answer3Set = false;
                        if(answer1Set){
                            answer1TextView.setBackgroundColor(Color.YELLOW);
                        } else {
                            answer1TextView.setBackgroundColor(Color.TRANSPARENT);
                        }

                        if(answer2Set){
                            answer2TextView.setBackgroundColor(Color.YELLOW);
                        } else {
                            answer2TextView.setBackgroundColor(Color.TRANSPARENT);
                        }


                        if(answer3Set){
                            answer3TextView.setBackgroundColor(Color.YELLOW);
                        } else {
                            answer3TextView.setBackgroundColor(Color.TRANSPARENT);
                        }
                        if(questionList.get(index).hasImage()) {
                            try {
                                imageView.setImageBitmap(BitmapFactory.decodeStream(assetManager.open(that.getIntent().getStringExtra("categoria") + "/Data/" + questionList.get(index).getChapterName() + "/" + questionList.get(index).getNum() + ".jpg")));

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




        EditText editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        editTextNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    index = Integer.parseInt(s.toString()) - 1;
                } catch (Exception e){
                    index = -1;
                }

                if(index < 0 || index > questionList.size() - 1){
                    return;
                }

                questionTextView.setText(questionList.get(index).getNum() + ". " + questionList.get(index).getQuestion());

                answer1TextView.setText(questionList.get(index).getAnswers()[0]);

                answer2TextView.setText(questionList.get(index).getAnswers()[1]);

                answer3TextView.setText(questionList.get(index).getAnswers()[2]);

                buttonA.setBackgroundColor(getResources().getColor(R.color.light_blue));
                buttonB.setBackgroundColor(getResources().getColor(R.color.light_blue));
                buttonC.setBackgroundColor(getResources().getColor(R.color.light_blue));

                answer1Set = false;
                answer2Set = false;
                answer3Set = false;

                if(answer1Set){
                    answer1TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer1TextView.setBackgroundColor(Color.TRANSPARENT);
                }

                if(answer2Set){
                    answer2TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer2TextView.setBackgroundColor(Color.TRANSPARENT);
                }


                if(answer3Set){
                    answer3TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer3TextView.setBackgroundColor(Color.TRANSPARENT);
                }

                if(questionList.get(index).hasImage()) {
                    try {
                        imageView.setImageBitmap(BitmapFactory.decodeStream(assetManager.open(that.getIntent().getStringExtra("categoria")+ "/Data/" + questionList.get(index).getChapterName() + "/" + questionList.get(index).getNum() + ".jpg")));

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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        answer1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer1Set = !answer1Set;

                if(answer1Set){
                    buttonA.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else {
                    buttonA.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }

                if(answer1Set){
                    answer1TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer1TextView.setBackgroundColor(Color.TRANSPARENT);
                }

                if(answer2Set){
                    answer2TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer2TextView.setBackgroundColor(Color.TRANSPARENT);
                }


                if(answer3Set){
                    answer3TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer3TextView.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        answer2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer2Set = !answer2Set;

                if(answer2Set){
                    buttonB.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else {
                    buttonB.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }

                if(answer1Set){
                    answer1TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer1TextView.setBackgroundColor(Color.TRANSPARENT);
                }

                if(answer2Set){
                    answer2TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer2TextView.setBackgroundColor(Color.TRANSPARENT);
                }


                if(answer3Set){
                    answer3TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer3TextView.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        answer3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer3Set = !answer3Set;

                if(answer3Set){
                    buttonC.setBackgroundColor(getResources().getColor(R.color.yellow));
                } else {
                    buttonC.setBackgroundColor(getResources().getColor(R.color.light_blue));
                }

                if(answer1Set){
                    answer1TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer1TextView.setBackgroundColor(Color.TRANSPARENT);
                }

                if(answer2Set){
                    answer2TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer2TextView.setBackgroundColor(Color.TRANSPARENT);
                }


                if(answer3Set){
                    answer3TextView.setBackgroundColor(Color.YELLOW);
                } else {
                    answer3TextView.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }
}
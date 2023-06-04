package com.example.quizz.ui;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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

import com.example.quizz.MainActivity;
import com.example.quizz.R;
import com.example.quizz.logic.Category;
import com.example.quizz.logic.Chapter;
import com.example.quizz.logic.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Training#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Training extends Fragment {

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

    public Training() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Training.
     */
    // TODO: Rename and change types and number of parameters
    public static Training newInstance(String param1, String param2) {
        Training fragment = new Training();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity activity = (MainActivity) getActivity();
        Category c = activity.getCategory();
        Map<String, Chapter> map = c.getChapterMap();

        List<String> spinnerArray =  new ArrayList<String>();
        for(Map.Entry<String, Chapter> entry : map.entrySet()) {
            spinnerArray.add(entry.getKey());
        }


        View view = inflater.inflate(R.layout.fragment_training, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinner.setAdapter(adapter);

        questionList = map.get(spinnerArray.get(0)).getQuestionList();


        this.answered = new String[this.questionList.size()];
        for(int i = 0; i < this.answered.length; i++){
            this.answered[i] = "Not Answered";
        }


        final TextView questionTextView = (TextView)view.findViewById(R.id.question);
        questionTextView.setText(this.questionList.get(0).getQuestion());

        final TextView answer1TextView = (TextView)view.findViewById(R.id.answer1);
        answer1TextView.setText(this.questionList.get(0).getAnswers()[0]);

        final TextView answer2TextView = (TextView)view.findViewById(R.id.answer2);
        answer2TextView.setText(this.questionList.get(0).getAnswers()[1]);

        final TextView answer3TextView = (TextView)view.findViewById(R.id.answer3);
        answer3TextView.setText(this.questionList.get(0).getAnswers()[2]);

        ImageButton nextQuestion = (ImageButton) view.findViewById(R.id.nextQuestion);
        final String[] ans = this.answered;

        final TextView initialQuestions = (TextView)view.findViewById(R.id.initial_questions);
        initialQuestions.setText(questionList.size() + " Intrebari initiale");

        final TextView remainingQuestionsView = (TextView)view.findViewById(R.id.remaining_questions);
        remainingQuestionsView.setText(questionList.size() + " Intrebari ramase");

        final TextView correctAnswersView = (TextView)view.findViewById(R.id.correct_answers);

        final TextView wrongAnswersView = (TextView)view.findViewById(R.id.wrong_answers);

        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        final AssetManager assetManager = this.getActivity().getAssets();
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

        final Button buttonA = (Button) view.findViewById(R.id.buttonA);
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

        final Button buttonB = (Button) view.findViewById(R.id.buttonB);
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

        final Button buttonC = (Button) view.findViewById(R.id.buttonC);
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

        final ImageButton buttonDeleteAnswer = (ImageButton) view.findViewById(R.id.deleteAnswer);
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

        final ImageButton sendAnswer = (ImageButton) view.findViewById(R.id.sendAnswer);
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

                        remainingQuestionsView.setText((questionList.size() - noAnswered) + " Intrebari ramase");

                        correctAnswersView.setText((correctAnswered) + " Raspunsuri corecte");

                        wrongAnswersView.setText((noAnswered-correctAnswered) + " Raspunsuri gresite");


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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = spinnerArray.get(position);
                questionList = map.get(selected).getQuestionList();

                index = 0;
                questionTextView.setText("1. " + questionList.get(0).getQuestion());

                answer1TextView.setText(questionList.get(0).getAnswers()[0]);

                answer2TextView.setText(questionList.get(0).getAnswers()[1]);

                answer3TextView.setText(questionList.get(0).getAnswers()[2]);

                buttonA.setBackgroundColor(getResources().getColor(R.color.light_blue));
                buttonB.setBackgroundColor(getResources().getColor(R.color.light_blue));
                buttonC.setBackgroundColor(getResources().getColor(R.color.light_blue));

                answer1Set = false;
                answer2Set = false;
                answer3Set = false;

                initialQuestions.setText(questionList.size() + " Intrebari initiale");

                remainingQuestionsView.setText(questionList.size() + " Intrebari ramase");

                correctAnswersView.setText("0 Raspunsuri corecte");

                wrongAnswersView.setText("0 Raspunsuri gresite");

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        EditText editTextNumber = (EditText) view.findViewById(R.id.editTextNumber);
        editTextNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                index = Integer.parseInt(s.toString()) - 1;

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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
}
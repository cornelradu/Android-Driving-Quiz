package com.example.quizz.ui;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizz.MainActivity;
import com.example.quizz.R;
import com.example.quizz.databinding.FragmentNotificationsBinding;
import com.example.quizz.logic.Category;
import com.example.quizz.logic.Question;
import com.example.quizz.logic.Quiz;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Simulation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Simulation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentNotificationsBinding binding;

    int index = 0;

    List< Question> questionList;

    String[] answered;

    int maxWrong = 4;
    int noAnswered = 0;
    int correctAnswered = 0;

    int time = 0;

    boolean answer1Set = false;
    boolean answer2Set = false;
    boolean answer3Set = false;

    public Simulation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Simulation.
     */
    // TODO: Rename and change types and number of parameters
    public static Simulation newInstance(String param1, String param2) {
        Simulation fragment = new Simulation();
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

    void finish(View view, int wrongNumb, int correctNumb, String result){
        Bundle bundle = new Bundle();
        bundle.putString("wrong", "" + wrongNumb);
        bundle.putString("correct", "" + correctNumb);
        bundle.putString("result", result);

        Navigation.findNavController(view).navigate(R.id.navigation_home, bundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity activity = (MainActivity) getActivity();
        Category c = activity.getCategory();
        this.questionList = Quiz.generateQuiz(c);
        this.answered = new String[c.getNoQuestions()];
        final int noOfQ = c.getNoQuestions();
        for(int i = 0; i < this.answered.length; i++){
            this.answered[i] = "Not Answered";
        }

        time = 30 * 60;

        View view = inflater.inflate(R.layout.fragment_simulation, container, false);

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
        initialQuestions.setText(noOfQ + " Intrebari initiale");

        final TextView remainingQuestionsView = (TextView)view.findViewById(R.id.remaining_questions);
        remainingQuestionsView.setText(noOfQ + " Intrebari ramase");

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

                        remainingQuestionsView.setText((noOfQ - noAnswered) + " Intrebari ramase");

                        correctAnswersView.setText((correctAnswered) + " Raspunsuri corecte");

                        wrongAnswersView.setText((noAnswered-correctAnswered) + " Raspunsuri gresite");

                        if(noAnswered - correctAnswered > maxWrong){
                            finish(view, noAnswered - correctAnswered, correctAnswered, "Failed");
                            return true;
                        } else if(noAnswered == noOfQ){
                            finish(view, noAnswered - correctAnswered, correctAnswered, "Succeded");
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
        final TextView timeRemainingVIew = (TextView)view.findViewById(R.id.remaining_time);
        final Runnable r = new Runnable() {
            public void run() {
                time -= 1;
                timeRemainingVIew.setText(time/3600 + ":" + (time/60) + ":" + (time%60) + " Timp ramas");
                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);


        return view;
    }
}
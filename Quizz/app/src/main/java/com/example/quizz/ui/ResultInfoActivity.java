package com.example.quizz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.quizz.R;

public class ResultInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_info);

        int wrong = getIntent().getIntExtra("wrong", 0);

        int correct = getIntent().getIntExtra("correct", 0);
        String result = getIntent().getStringExtra("result");

        final TextView questionTextView = (TextView)findViewById(R.id.textView);
        String text = "";
        if(result.equals("Succeded")) {
             text = "Test trecut. Ati raspuns corect la " + correct + " intrebari si gresit la " + wrong + " intrebari.";
        } else {
            text = "Test picat. Ati raspuns corect la " + correct + " intrebari si gresit la " + wrong + " intrebari.";
        }
        questionTextView.setText(text);

    }

    @Override
    public void onBackPressed() {
        // Handle back button press (optional)
        // This will perform the same action as clicking the button to go back to Activity A
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
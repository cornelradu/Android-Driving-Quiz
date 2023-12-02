package com.example.quizz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
             text = "Admis.";
        } else {
            text = "Respins.";
        }
        questionTextView.setText(text);

        final TextView raspunsuriCorecte = (TextView)findViewById(R.id.raspunsuriCorecte);
        raspunsuriCorecte.setText(correct + " raspunsuri corecte.");

        final TextView raspunsuriGresite = (TextView)findViewById(R.id.raspunsuriGresite);
        raspunsuriGresite.setText(wrong + " raspunsuri gresite.");

        final Button buttonExamineaza = (Button)findViewById(R.id.buttonExamineaza);
        buttonExamineaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SecondActivity when the button is clicked
                Intent intent = new Intent(ResultInfoActivity.this, ExamineazaRaspunsuriGresite.class);
                intent.putExtra("questionIds", getIntent().getIntArrayExtra("questionIds"));
                intent.putExtra("answered", getIntent().getStringArrayExtra("answered"));
                intent.putExtra("wrong", getIntent().getIntExtra("wrong", 0));
                intent.putExtra("categoria", getIntent().getStringExtra("categoria"));
                intent.putExtra("answeredCodes", getIntent().getIntArrayExtra("answeredCodes"));
                startActivityForResult(intent, 1);
            }
        });

        if(wrong == 0){
            buttonExamineaza.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        // Handle back button press (optional)
        // This will perform the same action as clicking the button to go back to Activity A
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Intent intent = new Intent();
            setResult(resultCode, intent);
            finish();
        super.onBackPressed();
    }
}
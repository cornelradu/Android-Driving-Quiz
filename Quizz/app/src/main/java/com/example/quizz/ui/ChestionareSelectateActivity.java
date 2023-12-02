package com.example.quizz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.quizz.R;
import com.example.quizz.logic.Category;
import com.example.quizz.logic.Chapter;

import java.util.Map;

public class ChestionareSelectateActivity extends AppCompatActivity {

    void createButton(int index, String category){
        Button button = new Button(this);
        button.setText("Chestionar " + index);

        // Set any additional properties for the button if needed

        // Create layout parameters to define how the button will be displayed
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                400,
                200
        );

        layoutParams.leftMargin = 50 + 500 * ((index - 1) % 2);
        layoutParams.topMargin = 50 + 220 * ((index - 1) / 2);
        // Add the button to the layout of the activity
        ViewGroup rootLayout = findViewById(R.id.form_layout);
        rootLayout.addView(button, layoutParams);

        button.setBackgroundColor(getResources().getColor(R.color.purple_500)); // Set background color
        button.setTextColor(getResources().getColor(android.R.color.white)); // Set text color


        // Set OnClickListener for the programmatically created button
        button.setOnClickListener(v -> {
            Intent intent = new Intent(ChestionareSelectateActivity.this, SimulareActivity.class);
            intent.putExtra("chestionar", index);
            intent.putExtra("categoria", category);
            startActivity(intent);

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chestionare_selectate);

        AssetManager assetManager = this.getAssets();

        String categoryStr = this.getIntent().getStringExtra("categoria");
        Category category = new Category(assetManager, categoryStr);

        int num = 0;
        if(categoryStr.equals("Categoria A")){
            num = 21;
        } else if (categoryStr.equals("Categoria B")){
            num = 60;
        }

        for(int index = 1; index <= num; index++){
            createButton(index, categoryStr);
        }
    }
}
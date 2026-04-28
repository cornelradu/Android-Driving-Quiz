package com.dmradu.quiz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.dmradu.quiz.R;
import com.dmradu.quiz.logic.Category;
import com.google.android.material.button.MaterialButton;

public class ChestionareSelectateActivity extends AppCompatActivity {

    void createButton(int index, String category) {
        MaterialButton button = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle);
        button.setText("Chestionar " + index);
        button.setAllCaps(false);
        button.setTextSize(14);
        button.setPadding(16, 16, 16, 16);
        button.setCornerRadius(24);

        // Grid layout parameters
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
        params.setMargins(16, 16, 16, 16);
        params.setGravity(Gravity.FILL);

        // Add the button to the layout
        ViewGroup rootLayout = findViewById(R.id.form_layout);
        rootLayout.addView(button, params);

        // Set OnClickListener
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
        
        int num = 0;
        if (categoryStr != null) {
            if (categoryStr.equals("Categoria A")) {
                num = 21;
            } else if (categoryStr.equals("Categoria B")) {
                num = 60;
            }
        }

        for (int index = 1; index <= num; index++) {
            createButton(index, categoryStr);
        }
    }
}
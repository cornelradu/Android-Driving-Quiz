package com.example.quizz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.example.quizz.R;
import com.example.quizz.logic.Category;
import com.example.quizz.logic.Chapter;
import com.google.android.material.button.MaterialButton;

import java.util.Map;

public class MediuDeInvatareActivity extends AppCompatActivity {

    void createButton(String chapterName, int index) {
        String originalChapterName = "" + chapterName;
        chapterName = chapterName.replace(" ", "\n");
        
        MaterialButton button = new MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle);
        button.setText(chapterName);
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
            Intent intent = new Intent(MediuDeInvatareActivity.this, AntrenamentActivity.class);
            intent.putExtra("chapter_name", originalChapterName);
            intent.putExtra("categoria", this.getIntent().getStringExtra("categoria"));
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediu_de_invatare);

        AssetManager assetManager = this.getAssets();
        Category category = new Category(assetManager, this.getIntent().getStringExtra("categoria"));
        Map<String, Chapter> chapters = category.getChapterMap();
        int index = 0;
        for (Map.Entry<String, Chapter> entry : chapters.entrySet()) {
            createButton(entry.getKey(), index);
            index++;
        }
    }
}
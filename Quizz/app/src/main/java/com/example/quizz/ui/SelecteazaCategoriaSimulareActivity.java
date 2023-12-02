package com.example.quizz.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quizz.MainActivity;
import com.example.quizz.R;

public class SelecteazaCategoriaSimulareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecteaza_categoria_simulare);

        final Button buttonA = (Button)findViewById(R.id.buttonCategoriaA);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SecondActivity when the button is clicked
                Intent intent = new Intent(SelecteazaCategoriaSimulareActivity.this, SimulareActivity.class);
                intent.putExtra("chestionar", 0);
                intent.putExtra("categoria", "Categoria A");
                startActivity(intent);
            }
        });

        final Button buttonB = (Button)findViewById(R.id.buttonCategoriaB);
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SecondActivity when the button is clicked
                Intent intent = new Intent(SelecteazaCategoriaSimulareActivity.this, SimulareActivity.class);
                intent.putExtra("chestionar", 0);
                intent.putExtra("categoria", "Categoria B");
                startActivity(intent);
            }
        });
    }
}
package com.example.quizz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quizz.logic.Category;
import com.example.quizz.ui.ChestionareSelectateActivity;
import com.example.quizz.ui.MediuDeInvatareActivity;
import com.example.quizz.ui.SimulareActivity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizz.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button buttonSimulare = (Button)findViewById(R.id.buttonSimulare);
        buttonSimulare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SecondActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, SimulareActivity.class);
                intent.putExtra("chestionar", 0);
                startActivity(intent);
            }
        });

        final Button buttonMediuDeInvatare = (Button)findViewById(R.id.buttonMediuDeInvatare);
        buttonMediuDeInvatare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SecondActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, MediuDeInvatareActivity.class);
                startActivity(intent);
            }
        });

        final Button buttonChestionareSelectate = (Button)findViewById(R.id.buttonChestionareSelectate);
        buttonChestionareSelectate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SecondActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, ChestionareSelectateActivity.class);
                startActivity(intent);
            }
        });
    }

    public Category getCategory(){
        return this.category;
    }

}
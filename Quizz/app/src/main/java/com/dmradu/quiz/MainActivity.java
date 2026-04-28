package com.dmradu.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dmradu.quiz.logic.Category;
import com.dmradu.quiz.ui.ChestionareSelectateActivity;
import com.dmradu.quiz.ui.MediuDeInvatareActivity;
import com.dmradu.quiz.ui.SelecteazaCategoriaChestionareSelectate;
import com.dmradu.quiz.ui.SelecteazaCategoriaMediuDeInvatare;
import com.dmradu.quiz.ui.SelecteazaCategoriaSimulareActivity;
import com.dmradu.quiz.ui.SimulareActivity;

import androidx.appcompat.app.AppCompatActivity;

import com.dmradu.quiz.databinding.ActivityMainBinding;

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
                Intent intent = new Intent(MainActivity.this, SelecteazaCategoriaSimulareActivity.class);
                intent.putExtra("chestionar", 0);
                startActivity(intent);
            }
        });

        final Button buttonMediuDeInvatare = (Button)findViewById(R.id.buttonMediuDeInvatare);
        buttonMediuDeInvatare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SecondActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, SelecteazaCategoriaMediuDeInvatare.class);
                startActivity(intent);
            }
        });

        final Button buttonChestionareSelectate = (Button)findViewById(R.id.buttonChestionareSelectate);
        buttonChestionareSelectate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SecondActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, SelecteazaCategoriaChestionareSelectate.class);
                startActivity(intent);
            }
        });
    }

    public Category getCategory(){
        return this.category;
    }

}
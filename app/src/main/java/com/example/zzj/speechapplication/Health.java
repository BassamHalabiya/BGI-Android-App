package com.example.zzj.speechapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Health extends AppCompatActivity{
    TextView physical_exam;
    TextView personal_diet;
    TextView fitness_plan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        physical_exam = (TextView) findViewById(R.id.pe);
        personal_diet = (TextView) findViewById(R.id.diet);
        fitness_plan = (TextView) findViewById(R.id.fitness);

        physical_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_physical_exam);
            }
        });
        personal_diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_diet);
            }
        });
        fitness_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_fitness);
            }
        });
    }
}
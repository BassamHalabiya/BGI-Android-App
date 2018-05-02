package com.example.zzj.speechapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Processing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //SplashScreen is the name of the screen that splash will go to
                //Intent i = new Intent(Processing.this, MainMenu.class);
                GoogleSignInAccount account = getIntent().getParcelableExtra("ACCOUNT");
                Intent i = new Intent("com.example.zzj.speechapplication.MainMenu");
                i.putExtra("ACCOUNT", account);
                startActivity(i);
                finish();
            }
        }, 7000); // use 5000 for green dna;


    }

}

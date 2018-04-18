package com.example.sachiv.splashscreen;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

// main activity

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            //SplashScreen is the name of the screen that splash will go to
                Intent i = new Intent(Splash.this, SplashScreen.class);
                startActivity(i);
                finish();
            }
        }, 3000); // here 3000 is 3 seconds;


    }
}

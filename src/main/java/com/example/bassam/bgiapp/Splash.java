package com.example.bassam.bgiapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        image = (ImageView) findViewById(R.id.bgi_logo);

        imageAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //SplashScreen is the name of the screen that splash will go to
                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
                finish();
            }
        }, 3000); // here 3000 is 3 seconds;

    }

    public void imageAnimation(){
        Animation myFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        image.startAnimation(myFadeIn);

        // This is in case we want to fade out
        /*Animation img = new AlphaAnimation(1.00f, 0.00f);
        img.setDuration(5000);
        img.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                image.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                image.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        image.startAnimation(img); //this initiates the whole thing
        */
    }
}

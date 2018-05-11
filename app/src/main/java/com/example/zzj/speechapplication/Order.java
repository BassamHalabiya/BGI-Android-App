package com.example.zzj.speechapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browserpage);

    }

    public void amazon(View view){
        Intent amazonIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com"));
        startActivity(amazonIntent);
    }

    public void bgibutton(View view){
        Intent bgiIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bgi.com"));
        startActivity(bgiIntent);
    }
}
package com.example.zzj.speechapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import android.widget.Button;
import android.widget.GridLayout;

import android.widget.GridView;

import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainMenu extends AppCompatActivity{
    protected Intent mainIntent;
    protected Intent reportIntent;
    protected Intent orderintent;
    protected Intent healthintent;
    GridLayout mainGrid;
    Button button_report;
    Button button_question;
    Button button_order;
    Button button_health;

    private static final String TAG = "Menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        reportIntent = new Intent("com.example.zzj.speechapplication.Report");
        mainIntent = new Intent("com.example.zzj.speechapplication.MainActivity");
        orderintent = new Intent("com.example.zzj.speechapplication.Order");
        healthintent = new Intent("com.example.zzj.speechapplication.Health");

        button_order = (Button) findViewById(R.id.orderbutton);
        button_report = (Button) findViewById(R.id.reportbutton);
        button_health = (Button) findViewById(R.id.healthtipsbutton);
        button_question = (Button) findViewById(R.id.questionbutton);

        final GoogleSignInAccount account = getIntent().getParcelableExtra("ACCOUNT");
        final String highlights = getIntent().getStringExtra("HIGHLIGHT");
        button_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportIntent.putExtra("HIGHLIGHT", highlights);
                Log.e(TAG, "Highlights: " + highlights);
                reportIntent.putExtra("ACCOUNT", account);
                startActivity(reportIntent);
            }
        });

        button_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(healthintent);
            }
        });

        button_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(orderintent);
            }
        });


        button_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainIntent);
            }
        });
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

    }
}

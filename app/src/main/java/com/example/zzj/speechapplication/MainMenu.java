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

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import android.widget.Button;
import android.widget.GridLayout;

import android.widget.GridView;

import android.widget.Toast;

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
    GridLayout mainGrid;
    Button button_report;
    Button button_question;
    private static final int MY_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mainIntent = new Intent("com.example.zzj.speechapplication.MainActivity");
        button_report = (Button) findViewById(R.id.reportbutton);

        button_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reportpage();
            }
        });

        button_question = (Button) findViewById(R.id.questionbutton);

        button_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainIntent);
            }
        });
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

    }
}

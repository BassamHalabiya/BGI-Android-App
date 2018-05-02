package com.example.theodhor.speechapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

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


import java.util.Scanner;
import java.io.*;


public class report extends AppCompatActivity {

    private String[] names = new String[118];
    private String[] results = new String[118];
    private String[] tags1 = new String[118];
    private String[] tags2= new String[118];

    TextView highlights_box;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        highlights_box = findViewById(R.id.highlight_textbox);
        //highlights_box.setText("One TextView to rule them all");
        try {
            DataInputStream file2 = new DataInputStream(getAssets().open("Disease_results.csv"));
            Scanner input = new Scanner(file2);
            split(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final StringBuilder highlights = new StringBuilder();
        String line;

        int disease = 0;
        for (int i = 1; i < results.length; i++) {
            if(!results[i].equalsIgnoreCase("negative")) {
                disease++;
//                        highlights_box.append("You are " + results[i] + " for " + names[i]);
                highlights.append("You are " + results[i] + " for " + names[i]).append('\n');
                try{
                    Thread.sleep(5500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("-----------------------Number of diseases: " + disease);
        if (disease == 0) {
            highlights.append("Congratulations! Your test results came back negative for all diseases").append('\n');
            //highlights_box.setText("Congratulations! Your test results came back negative for all diseases");
        } else {
            highlights.append("This is the end of your report summary!").append('\n');
            //highlights_box.setText("This is the end of your report summary!");
        }
        highlights_box.setText(highlights.toString());
    }

    private void split(Scanner input) {
        int index = 0;
/*        String[] firstLine = input.nextLine().split(",");
        for (int i  = 0; i < firstLine.length - 1; i++) {
            for (int j  = 0; j < firstLine.length - 1; j++) {
                if( firstLine[j].contains("disease")){
                    names[index] = cells[j];
                }
            }
            if ()
        }*/
        while(input.hasNextLine()) {
            String line = input.nextLine();
            String[] cells = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            names[index] = cells[0];
            results[index] = cells[3];
            tags1[index] = cells[4];
            tags2[index] = cells[5];
            index++;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}

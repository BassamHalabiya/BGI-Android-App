package com.example.zzj.speechapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Processing extends AppCompatActivity {

    private static final String TAG = "Processing";
    private String[] names = new String[118];
    private String[] results = new String[118];
    private String[] tags1 = new String[118];
    private String[] tags2= new String[118];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //SplashScreen is the name of the screen that splash will go to
                StringBuilder highlights = buildHighlights();
                GoogleSignInAccount account = getIntent().getParcelableExtra("ACCOUNT");
                Intent i = new Intent("com.example.zzj.speechapplication.MainMenu");
                i.putExtra("HIGHLIGHT", highlights.toString());
                Log.e(TAG, "Hightlights: " + highlights.toString());
                i.putExtra("ACCOUNT", account);
                startActivity(i);
                finish();
            }
        }, 7000); // use 5000 for green dna;


    }

    private StringBuilder buildHighlights() {
        Context context = this;
        try {
            //File file = new File(Environment.getExternalStorageDirectory()+ "/Disease_results.csv");
            //File file = new File(Environment.getDataDirectory()+ "/Disease_results.csv");
            //InputStream fis = getAssets().open("Disease_results.csv");
            FileInputStream fis =context.openFileInput("Disease_results.csv");
            BufferedReader input = new BufferedReader(new InputStreamReader(fis));
            //PrintStream output = new PrintStream(new File("Cleaned_Data.csv"));
            FileOutputStream output = openFileOutput("Cleaned_Data.csv", Context.MODE_PRIVATE);
            cleanData(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fis = context.openFileInput("Cleaned_Data.csv");
            BufferedReader input = new BufferedReader(new InputStreamReader(fis));
            split(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final StringBuilder highlights = new StringBuilder();

        int disease = 0;
        for (int i = 1; i < results.length; i++) {
            if(!results[i].equalsIgnoreCase("negative")) {
                disease++;
                highlights.append("You are " + results[i] + " for " + names[i]).append('\n');
            }
        }
        if (disease == 0) {
            highlights.append("Congratulations! Your test results came back negative for all diseases").append('\n');
        } else {
            highlights.append("This is the end of your report summary!").append('\n');
        }
        return highlights;
    }

    private void cleanData(BufferedReader input, FileOutputStream output) throws IOException {
        String line = input.readLine();
        while (line != null) {
            String cleanLine = "";
            cleanLine = line.replaceAll("\'", "");
            cleanLine = cleanLine.replaceAll("-", " ");
            cleanLine = cleanLine.replaceAll("/", " or ");
            if (cleanLine.contains("?")) {
                if (cleanLine.contains("Wolfram")) {
                    cleanLine = cleanLine.replaceAll("\\?", " ");
                } else if (cleanLine.contains("poprotein")) {
                    cleanLine = cleanLine.replaceAll("\\?", "beta ");
                }
            }
            Log.e(TAG, cleanLine);
            output.write((cleanLine+"\n").getBytes());
            line = input.readLine();
        }
    }

    private void split(BufferedReader input) throws IOException {
        int index = 0;
        String line = input.readLine();
        while(line != null) {
            String[] cells = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            names[index] = cells[0];
            results[index] = cells[3];
            tags1[index] = cells[4];
            tags2[index] = cells[5];
            index++;
            line = input.readLine();
        }
    }
}

package com.example.sachiv.menu2;

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

public class MainActivity extends AppCompatActivity {
    GridLayout mainGrid;
    Button button;
    Button download_button;
    private static final int MY_PERMISSION = 1;
    ProgressDialog mProgressDialog;
    double file_size = 0;
    String file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.reportbutton);
        download_button = (Button) findViewById(R.id.downloadbutton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportpage();
            }
        });
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION);
                } else {
                    File dir =  new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/MyDownloadedFiles/" + file_name);
                    try {
                        dir.mkdir();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"Cannot create folder!",Toast.LENGTH_SHORT).show();
                    }
                    //file location
                    new DownloadTask().execute("http://www.wingnity.com/files/Hall_of_the_Mountain_King.mp3");

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void reportpage() {
        Intent startNewActivity = new Intent(this, report.class);
        startActivity(startNewActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            file_name = strings[0].substring(strings[0].lastIndexOf("/") + 1);
            try {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(strings[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP" + connection.getResponseCode() + " " +
                                connection.getResponseMessage();
                    }
                    int fileLength = connection.getContentLength();
                    file_size = fileLength;

                    input = connection.getInputStream();
                    output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/MyDownloadedFiles/" + file_name);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        if (isCancelled()) {
                            return null;
                        }
                        total += count;
                        if (fileLength > 0) {
                            publishProgress((int) (total * 100 / fileLength));
                        }
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    return e.toString();
                } finally {
                    try {
                        if (output != null) {
                            output.close();
                        }
                        if (input != null) {
                            input.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            } finally {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("downloading...");
            mProgressDialog.setMessage("File size: 0 MB");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);

            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(MainActivity.this, "Download cancelled", Toast.LENGTH_SHORT).show();
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/MyDownloadedFiles/" + file_name);

                    try {
                        dir.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(values[0]);
            mProgressDialog.setMessage("File size: " + new DecimalFormat("##.##").format(file_size / 1000000) + " MB");

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();
            if (result != null) {
                Toast.makeText(MainActivity.this, "Error: " + result, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "Downloaded!" + result, Toast.LENGTH_SHORT).show();

            }
        }
    }
}










package com.example.bassam.bgiapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Report extends AppCompatActivity {
    Button download_button;
    GoogleSignInAccount account;
    protected DriveClient mDriveClient;
    protected DriveResourceClient mDriveResourceClient;
    protected DriveId mDriveId;
    private static final String TAG = "Report";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        download_button = (Button) findViewById(R.id.downloadbutton);
        account = getIntent().getParcelableExtra("ACCOUNT");
        Log.e(TAG, "Account: " + account.toString());
        mDriveClient = Drive.getDriveClient(getApplicationContext(), account);
        mDriveResourceClient = Drive.getDriveResourceClient(getApplicationContext(), account);
        Log.e(TAG, "Client: " + mDriveClient.toString());
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
    }

    private void download() {
        Query query = new Query.Builder()
                //.addFilter(Filters.eq(SearchableField.MIME_TYPE, "application/pdf"))
                .addFilter(Filters.contains(SearchableField.TITLE, "Disease_results"))
                .build();
        Log.e(TAG, "Check query condition: " + query.getFilter());
        Task<MetadataBuffer> queryTask = mDriveResourceClient.query(query);
        Log.e(TAG, "Check query contents " + query.describeContents());
        queryTask
                .addOnSuccessListener(this, new OnSuccessListener<MetadataBuffer>() {
                    @Override
                    public void onSuccess(MetadataBuffer metadataBuffer) {
                        // Handle results...
                        Log.e(TAG, "Number of metadata " + metadataBuffer.getCount());
                        mDriveId = metadataBuffer.get(0).getDriveId();
                        Task<DriveContents> openFileTask =
                                mDriveResourceClient.openFile(mDriveId.asDriveFile(), DriveFile.MODE_READ_ONLY);
                        Log.e(TAG, "File ID: " + mDriveId.encodeToString());
                        // [START read_contents]
                        openFileTask
                                .continueWithTask(new Continuation<DriveContents, Task<Void>>() {
                                    @Override
                                    public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {
                                        DriveContents contents = task.getResult();
                                        // Process contents...
                                        // [START_EXCLUDE]
                                        // [START read_as_string]
                                        try {
                                            BufferedReader input = new BufferedReader(
                                                    new InputStreamReader(contents.getInputStream()));
                                            StringBuilder data = new StringBuilder();
                                            String line;
                                            while ((line = input.readLine()) != null) {
                                                data.append(line).append('\n');
                                            }

                                            try {
                                                FileOutputStream output = openFileOutput(Environment.getExternalStorageDirectory()
                                                        + File.separator+ "Report.pdf",Context.MODE_PRIVATE);
                                                OutputStreamWriter writer = new OutputStreamWriter(output);
                                                writer.write(data.toString());
                                                writer.close();
                                            } catch (IOException e) {
                                                Log.e(TAG, "Failed to download file.");
                                            }

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        // [END read_as_string]
                                        // [START discard_contents]
                                        Task<Void> discardTask = mDriveResourceClient.discardContents(contents);
                                        // [END discard_contents]
                                        return discardTask;
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle failure
                                        // [START_EXCLUDE]
                                        Log.e(TAG, "Unable to read contents", e);
                                        finish();
                                        // [END_EXCLUDE]
                                    }
                                });
                        // [END read_contents]
                        Log.e("Drive", "Querying file" + mDriveId.encodeToString());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure...
                        Log.e(TAG, "Unable to query", e);
                    }
                });
    }

}

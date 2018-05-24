package com.example.bassam.bgiapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.AlarmClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.io.*;

public class MainActivity extends AppCompatActivity{

    private TextToSpeech tts;
    private ArrayList<String> questions;
    private String name, surname, age, asName;
    private String[] names = new String[118];
    private String[] results = new String[118];
    private String[] tags1 = new String[118];
    private String[] tags2= new String[118];
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String NEW = "new";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String AS_NAME = "as_name";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();
        Context context = this;
        final GoogleSignInAccount account = getIntent().getParcelableExtra("ACCOUNT");
        //String fileId = "Disease_results.csv";
        //DriveId driveId = DriveId.decodeFromString(fileId);
        //final DriveResourceClient client = Drive.getDriveResourceClient(getApplicationContext(), account);
        /*Task<DriveContents> openFileTask =
                client.openFile(driveId.asDriveFile(), DriveFile.MODE_READ_ONLY);

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
                            FileOutputStream output = openFileOutput("Cleaned_Data.csv", Context.MODE_PRIVATE);
                            cleanData(input, output);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // [END read_as_string]
                        // [START discard_contents]
                        Task<Void> discardTask = client.discardContents(contents);
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
        // [END read_contents]*/

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

        findViewById(R.id.microphoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen();
            }
        });
        loadQuestions();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak("Hello");

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

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

    private void loadQuestions(){
        questions = new ArrayList<>();
        questions.clear();
        questions.add("Hello, what is your name?");
        questions.add("What is your surname?");
        questions.add("How old are you?");
        questions.add("That's all I had, thank you ");
    }

    private void listen(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, "Your device doesn't support Speech Recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        }else{
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String inSpeech = res.get(0);
                recognition(inSpeech);
            }
        }
    }

    private void recognition(String text) {
        Log.e("Speech",""+text);
        String[] speech = text.split(" ");
        if(text.contains("hello")){
            speak(questions.get(0));
        }
        //
        if(text.contains("my name is")){
            name = speech[speech.length-1];
            Log.e("THIS", "" + name);
            editor.putString(NAME,name).apply();
            speak(questions.get(2));
        }
        //This must be the age
        if(text.contains("years") && text.contains("old")){
            String age = speech[speech.length-3];
            Log.e("THIS", "" + age);
            editor.putString(AGE, age).apply();
        }

        if(text.contains("what time is it")){
            SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm");//dd/MM/yyyy
            Date now = new Date();
            String[] strDate = sdfDate.format(now).split(":");
            if(strDate[1].contains("00"))
                strDate[1] = "o'clock";
            speak("The time is " + sdfDate.format(now));

        }

        if(text.contains("wake me up at")){
            speak(speech[speech.length-1]);
            String[] time = speech[speech.length-1].split(":");
            String hour = time[0];
            String minutes = time[1];
            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            i.putExtra(AlarmClock.EXTRA_HOUR, Integer.valueOf(hour));
            i.putExtra(AlarmClock.EXTRA_MINUTES, Integer.valueOf(minutes));
            startActivity(i);
            speak("Setting alarm to ring at " + hour + ":" + minutes);
        }

        if(text.contains("thank you")){
            speak("Thank you too " + preferences.getString(NAME, null));
        }

        if(text.contains("how old am I")){
            speak("You are "+preferences.getString(AGE,null)+" years old.");
        }

        if(text.contains("what is your name")){
            String as_name = preferences.getString(AS_NAME,"");
            if(as_name.equals(""))
                speak("How do you want to call me?");
            else
                speak("My name is "+as_name);
        }

        if(text.contains("call you")){
            String name = speech[speech.length-1];
            editor.putString(AS_NAME,name).apply();
            speak("I like it, thank you "+preferences.getString(NAME,null));
        }

        if(text.contains("what is my name")){
            speak("Your name is "+preferences.getString(NAME,null));
        }

        if(text.contains("highlights") || text.contains("summary")) {
            int disease = 0;
            for (int i = 1; i < results.length; i++) {
                if(!results[i].equalsIgnoreCase("negative")) {
                    disease++;
                    speak("You are " + results[i] + " for " + names[i]);
                    try{
                        Thread.sleep(5500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("-----------------------Number of diseases: " + disease);
            if (disease == 0) {
                speak("Congratulations! Your test results came back negative for all diseases");
            } else {
                speak("This is the end of your report summary!");

            }
        }

        if(text.contains("condition") || text.contains("disease")) {
            String symptom = speech[speech.length-2];
            int disease = 0;
            for(int i = 0; i < tags2.length; i++) {
                if(tags2[i].toLowerCase().contains(symptom)) {
                    if(!results[i].equalsIgnoreCase("negative")) {
                        disease++;
                        speak("You are a " + results[i] + " of " + names[i]);
                        try{
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (disease == 0) {
                speak("You do not have any " + speech[speech.length-2] + speech[speech.length-1]);
            }
        } else if(text.contains("risk") || text.startsWith("do I have")){
            int disease = 0;
            String symptom = speech[speech.length-1];
            for(int i = 0; i < results.length; i++) {
                if (tags1[i].toLowerCase().contains(symptom) && !results[i].equalsIgnoreCase("negative")) {
                    disease++;
                    speak("You are a " + results[i] + "of " + names[i]);
                    try {
                        Thread.sleep(5000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            if(disease == 0){
                speak("You do not have any potential for" + speech[speech.length-1]);
            }
        }

    }
}

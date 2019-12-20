package com.arashad96.andoriddeveloperadvancedkit;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class voice_event extends AppCompatActivity {
    Button save;
    Button help;
    Button cancel;
    ImageView voice;
    TextView text;

    String check = "";
    String output = "";
    String outputmonth = "";
    String outputday = "";
    String outputyear = "";
    String outputhour = "0";
    String outputminute = "0";

    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voice_event);

        text = findViewById(R.id.text);
        voice = findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();

            }
        });
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
                try {
                    if (output.equals("") || outputyear.equals("") || outputmonth.equals("") || outputday.equals("") || outputhour.equals("") || outputminute.equals("")) {
                        Toast.makeText(getBaseContext(), "You didn't follow the speaking protocol correctly please try again", Toast.LENGTH_LONG).show();

                    } else {

                        Intent i = new Intent(voice_event.this, showevent.class);
                        i.putExtra("eventname", output);
                        i.putExtra("year", Integer.parseInt(outputyear));
                        i.putExtra("month", Integer.parseInt(outputmonth));
                        i.putExtra("day", Integer.parseInt(outputday));
                        i.putExtra("hour", Integer.parseInt(outputhour));
                        i.putExtra("minute", Integer.parseInt(outputminute));
                        voice_event.this.finish();
                        startActivity(i);
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "You didn't follow the speaking protocol correctly please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
        help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(voice_event.this);
                builder.setMessage("Please say as follow to save your event correctly " +
                        "launch \"whatever the reminder for\" at \"Day\"\"Month\"\"Year\" at \"hour\":\"minute\" ")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voice_event.super.onBackPressed();
            }
        });

    }

    /*
      Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.app_name));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.app_name), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text.setText(result.get(0));

                    //just for now i will remove later
                    //check="make an event name test at 5/4 /2018 at 7";
                    check = text.getText().toString();
                }
                break;
            }
        }
    }

    public void check() {
        //0     1   2     3     4      5   6     7       8    9   10   11
        //make an event name "qwrasd" at "Day" "Month" "2018" at "7" "30"
        //0     1   2     3     4         5    6    7      8       9    10  11  12
        //make an event name "qwrasd" "qwrasd" at "Day" "Month" "2018"  at  "7" "30"
        // testing on how to parse info
        output = "";
        outputmonth = "";
        outputday = "";
        outputyear = "";
        outputhour = "0";
        outputminute = "0";

        if (check.contains("launch")) {
            String text[] = check.trim().split("at");

            //checking outputs
            for (int i = 0; i < text.length; i++) {
                Log.d("check1", "" + text[i]);
            }

            //whatever the size of the names I would handle the size of it using int x
            //int x = text[0].length();
            String textname[] = text[0].trim().split("\\s+");
            //in here i keep adding the names
            for (int i = 1; i < textname.length; i++) {
                output = output + " " + textname[i];
            }

            //String space = text[1].trim().replaceAll("\\s+","");

            String date[] = text[1].trim().split("\\s+");
            //checking outputs
            for (int i = 0; i < date.length; i++) {
                Log.d("check2", "" + date[i]);
            }
            outputday = date[0];
            outputmonth = date[1];
            outputyear = date[2];

            //check that they are all numbers
            try {
                int handlenumber1 = Integer.parseInt(outputday);
                int handlenumber2;// = Integer.parseInt(outputmonth);
                int handlenumber3 = Integer.parseInt(outputyear);

                //check that they donot go off limit
                if (handlenumber1 > 31) {
                    outputday = "31";
                }
                switch (outputmonth) {
                    case "January":
                        outputmonth = "1";
                        break;
                    case "February":
                        outputmonth = "2";
                        break;
                    case "March":
                        outputmonth = "3";
                        break;
                    case "April":
                        outputmonth = "4";
                        break;
                    case "May":
                        outputmonth = "5";
                        break;
                    case "June":
                        outputmonth = "6";
                        break;
                    case "July":
                        outputmonth = "7";
                        break;
                    case "August":
                        outputmonth = "8";
                        break;
                    case "September":
                        outputmonth = "9";
                        break;
                    case "October":
                        outputmonth = "10";
                        break;
                    case "November":
                        outputmonth = "11";
                        break;
                    case "December":
                        outputmonth = "12";
                        break;
                }
                if (handlenumber3 < 2019) {
                    outputyear = "2020";
                }
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "You didn't follow the speaking protocol correctly please try again", Toast.LENGTH_LONG).show();
            }

            //check time

            outputhour = text[2];
            Log.d("check3", "" + outputhour);

            if (outputhour.contains(":")) {
                String x[] = outputhour.trim().split(":");
                outputhour = x[0];
                outputminute = x[1];
                Log.d("x", "" + x);
                //check within the 24 hours
                int num = Integer.parseInt(outputhour);
                int num2 = Integer.parseInt(outputminute);
                if (num > 23) {
                    outputhour = "23";
                }
                if (num2 > 59) {
                    outputminute = "59";
                }
            } else {
                outputhour = text[2].trim();
                int num = Integer.parseInt(outputhour);
                if (num > 23) {
                    outputhour = "23";
                }
            }

            Log.d("check4", "" + outputhour);
            Log.d("check5", "min " + outputminute);

        } else {
            Toast.makeText(getBaseContext(), "You didn't follow the speaking protocol correctly please try again", Toast.LENGTH_LONG).show();
        }
    }
}
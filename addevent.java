package com.arashad96.andoriddeveloperadvancedkit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class addevent extends AppCompatActivity {
    EditText eventname;
    EditText datee;
    EditText time;
    Button save;
    Button cancel;
    int sendhour;
    int sendminute;
    final Calendar mycal = Calendar.getInstance();

    Calendar mcurrentTime = Calendar.getInstance();
    TimePickerDialog mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_addevent);


        eventname = findViewById(R.id.eventname);

        time = findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                //TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(addevent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(selectedHour + ":" + selectedMinute);
                        sendhour = selectedHour;
                        sendminute = selectedMinute;
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                mycal.set(Calendar.YEAR, year);
                mycal.set(Calendar.MONTH, monthOfYear);
                mycal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        datee = findViewById(R.id.date);
        datee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(addevent.this, date, mycal.get(Calendar.YEAR), mycal.get(Calendar.MONTH), mycal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = eventname.getText().toString();
                int year = mycal.get(Calendar.YEAR);
                int month = mycal.get(Calendar.MONTH);
                int day = mycal.get(Calendar.DAY_OF_MONTH);
                int hour = sendhour;
                int minute = sendminute;
                //send values to the upcoming event activity
                Intent i = new Intent(addevent.this, showevent.class);

                if (name.equals("") || datee.getText().toString().equals("") || time.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), "Some fields are required", Toast.LENGTH_LONG).show();

                } else {
                    month++;
                    i.putExtra("eventname", name);
                    i.putExtra("year", year);
                    i.putExtra("month", month);
                    i.putExtra("day", day);
                    i.putExtra("hour", hour);
                    i.putExtra("minute", minute);
                    addevent.this.finish();
                    startActivity(i);
                }

            }
        });
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addevent.super.onBackPressed();
            }
        });
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datee.setText(sdf.format(mycal.getTime()));
    }
}
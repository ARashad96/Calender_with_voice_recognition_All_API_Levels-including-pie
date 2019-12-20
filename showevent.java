package com.arashad96.andoriddeveloperadvancedkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class showevent extends AppCompatActivity {
    Button goback;
    Button help;
    ListView events;

    ArrayList<String> list = new ArrayList<String>();
    String name;

    int recieveday;
    int recievemonth;
    int recieveyear;
    int recievehour;
    int recieveminute;

    ArrayAdapter<String> adapter;
    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_showevent);
        openDB();
        events = findViewById(R.id.events);


        Bundle x = getIntent().getExtras();
        if(x!= null) {
            name = x.getString("eventname");
            recieveyear = x.getInt("year");
            recievemonth = x.getInt("month");
            recieveday = x.getInt("day");
            recievehour = x.getInt("hour");
            recieveminute = x.getInt("minute");

        }
        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showevent.this.finish();
            }
        });

        events.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {

                list.remove(position);
                myDb.deleteRow(position);
                adapter.notifyDataSetChanged();


                return false;
            }
        });

        help = findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(showevent.this);
                builder.setMessage("If you would like to remove an event have a long press on that event.")
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

        add();
        Cursor c = myDb.getAllRows();
        displayRecordSet(c);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }

    public void add(){
        long newId = myDb.insertRow(name, recieveday, recievemonth, recieveyear, recievehour, recieveminute);
    }
    // Display an entire recordset to the screen.
    private void displayRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                int day = cursor.getInt(DBAdapter.COL_DAY);
                int month = cursor.getInt(DBAdapter.COL_MONTH);
                int year = cursor.getInt(DBAdapter.COL_YEAR);
                int hour = cursor.getInt(DBAdapter.COL_HOUR);
                int minute = cursor.getInt(DBAdapter.COL_MIN);

                // [TO_DO_B6]
                // Create arraylist(s)? and use it(them) in the list view
                list.add("Name: " + name + "\n" + day + "/" + month + "/" + year + " " + hour + ":" + minute);

            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();


        // [TO_DO_B7]
        // Update the list view
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        events.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
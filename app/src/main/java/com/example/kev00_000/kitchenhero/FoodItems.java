package com.example.kev00_000.kitchenhero;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class FoodItems extends AppCompatActivity {

    //Hours, minutes and alarm ids
    private int h=0,m=0,Alarmnum=0;
    static final int DIALOG_ID = 0;

    //name of food to be passed
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_items);

        //Continue Button
        Button btn = (Button) findViewById(R.id.button1);

        //Button for time picked
        Button Timebtn = (Button) findViewById(R.id.TimeBtn);

        //establish all EditText boxes
        final EditText F1, F2, F3, F4, M1, M2, M3, M4;
        F1 = (EditText) findViewById(R.id.F1);
        F2 = (EditText) findViewById(R.id.F2);
        F3 = (EditText) findViewById(R.id.F3);
        F4 = (EditText) findViewById(R.id.F4);
        M1 = (EditText) findViewById(R.id.M1);
        M2 = (EditText) findViewById(R.id.M2);
        M3 = (EditText) findViewById(R.id.M3);
        M4 = (EditText) findViewById(R.id.M4);

        //When Continue is pressed
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                     public void onClick(View v) {


                        //If a time and name is entered for a food then create an alarm for it
                if (F1.getText().length() != 0 && M1.getText().length() != 0) {
                    name=F1.getText().toString();
                    Alarmnum=0;
                    onTimeSet(Integer.parseInt(M1.getText().toString()));
                }

                if (F2.getText().length() != 0 && M2.getText().length() != 0) {
                    name=F2.getText().toString();
                    Alarmnum=1;
                    onTimeSet(Integer.parseInt(M1.getText().toString()));
                }

                if (F3.getText().length() != 0 && M3.getText().length() != 0) {
                    name=F3.getText().toString();
                    Alarmnum=2;
                    onTimeSet(Integer.parseInt(M1.getText().toString()));
                }

                if (F4.getText().length() != 0 && M4.getText().length() != 0) {
                    name=F4.getText().toString();
                    Alarmnum=3;
                    onTimeSet(Integer.parseInt(M1.getText().toString()));
                }



            }

        });


        Timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });


    }

    //Hour and minute chosen from TimePicker stored and shown in toast
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    h = hourOfDay;
                    m = minute;

                    Toast.makeText(getApplicationContext(),"Time set for "+String.valueOf(h)+" : "+String.valueOf(m),Toast.LENGTH_SHORT).show();
                }
            };

    //Creates the Timepicker pop up
    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
        int h=c.get(Calendar.HOUR_OF_DAY);
        int m=c.get(Calendar.MINUTE);
        switch (id) {
            case DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, h, m, false);
        }
        return null;
    }


    //Sets the hour and minutes picked to actual time and takes cooking time away
    public void onTimeSet(int i) {
        if(i>m)
        {
            h--;
            m=m+60-i;
        }
        else
            m-=i;
        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, h);
        calSet.set(Calendar.MINUTE, m);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if(calSet.compareTo(calNow) <= 0){
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }

        setAlarm(calSet);
    }


        //Creates alarms
        private void setAlarm(Calendar targetCal){

        Toast.makeText(getApplicationContext(),
        "\n\n***\n"
        + "Alarm is set@ " + targetCal.getTime() + "\n"
        + "***\n",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("data",name);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),Alarmnum,intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

        }

}

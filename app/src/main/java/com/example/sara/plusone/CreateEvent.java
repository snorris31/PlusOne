package com.example.sara.plusone;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;

public class CreateEvent extends AppCompatActivity {

    Button setTime;
    Button setDate;
    TextView datePicker;
    TextView timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        timePicker = (TextView)findViewById(R.id.display_time);
        datePicker = (TextView)findViewById(R.id.display_date);
        setTime = (Button)findViewById(R.id.pick_time);
        setDate = (Button)findViewById(R.id.pick_date);


        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        String time = String.format()
                        timePicker.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(CreateEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int mYear = year;
                                int mMonth = monthOfYear;
                                int mDay = dayOfMonth;
                                datePicker.setText(new StringBuilder()
                                        // Month is 0 based so add 1
                                        .append(mMonth + 1).append("/").append(mDay).append("/")
                                        .append(mYear).append(" "));
                            }
                        }, mYear, mMonth, mDay);
                dialog.show();
            }
        });


    }


}

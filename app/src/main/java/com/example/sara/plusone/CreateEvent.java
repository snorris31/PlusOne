package com.example.sara.plusone;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.sara.plusone.objects.Event;
import com.firebase.client.Firebase;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import android.content.Context;
import java.util.Date;

public class CreateEvent extends AppCompatActivity {

    Button setTime;
    Button setDate;
    Button submitEvent;
    TextView datePicker;
    TextView timePicker;
    EditText nameEvent;
    Spinner eventType;
    String chosenPosition;
    String placeSelected;
    EditText descriptionEvent;
    View dateEvent;
    LinearLayout mainLayout;
    SimpleDateFormat newDate;
    PopupWindow popUp;
    TextView tv;
    Date finalDate;
    int mYear;
    int mMonth;
    int mDay;
    int hour;
    int minute;
    Dialog dialog;
    Firebase mFirebase;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timePicker = (TextView)findViewById(R.id.display_time);
        datePicker = (TextView)findViewById(R.id.display_date);
        setTime = (Button)findViewById(R.id.pick_time);
        setDate = (Button)findViewById(R.id.pick_date);
        submitEvent = (Button) findViewById(R.id.submit_event);
        mFirebase = new Firebase(MainActivity.FIREBASE_URL).child("events");

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
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.pick_location);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("Stuff", "Place: " + place.getName());
                placeSelected = (String) place.getName();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Error", "An error occurred: " + status);
            }
        });
        eventType = (Spinner)findViewById(R.id.eventType);
        eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenPosition = (String) parent.getItemAtPosition(position);
            }
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        dateEvent = findViewById(R.id.display_date);
        newDate = new SimpleDateFormat("MM/dd/yyyy");
        try {
            finalDate = newDate.parse(Integer.toString(mMonth)+"/"+Integer.toString(mDay)+"/"+Integer.toString(mYear));
        }catch (ParseException e) {
            e.printStackTrace();
        }
        nameEvent = (EditText) findViewById(R.id.eventTitle);
        descriptionEvent = (EditText) findViewById(R.id.editText);
        eventType = (Spinner)findViewById(R.id.eventType);
        eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenPosition = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        final Context context = this;
        submitEvent.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> arr = new ArrayList<String>();
            Firebase userRef = new Firebase(MainActivity.FIREBASE_URL);
            String userID = userRef.getAuth().getUid();

            @Override
            public void onClick(View v) {
                if (placeSelected == null || nameEvent.getText().toString().matches("") || descriptionEvent.getText().toString().matches("")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.popupview);
                    dialog.setTitle("Warning!");
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    System.out.println(descriptionEvent.toString());
                    Event event = new Event(userID, arr, chosenPosition, finalDate, placeSelected, timePicker.getText().toString(), nameEvent.getText().toString(), descriptionEvent.getText().toString(), false);
                    mFirebase.push().setValue(event);

                    submitEvent.setBackgroundColor(0x727272);
                    System.out.println(nameEvent.getText().toString());

                    submitEvent.setText("Submitted");
                    submitEvent.setClickable(false);
                    finish();
                }
            }
        });
    }
}

package com.example.sara.plusone.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sara.plusone.MainActivity;
import com.example.sara.plusone.R;
import com.example.sara.plusone.objects.Event;
import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;

/**
 * Created by Zack on 3/26/2016.
 */
public class EventViewListener implements View.OnClickListener {

    TextView titleField;
    TextView posterField;
    TextView typeField;
    TextView dateField;
    TextView addressField;
    TextView descriptionField;

    Button viewResponses;

    private LayoutInflater inflater;
    private Event event;
    private Context context;

    public EventViewListener(LayoutInflater inflater, Event event, Context context) {
        this.inflater = inflater;
        this.event = event;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        View detailView = inflater.inflate(R.layout.fragment_event_detail, null);

        titleField = (TextView)detailView.findViewById(R.id.title_field);
        titleField.setText(event.title);

        //TODO get poster info from database
        posterField = (TextView)detailView.findViewById(R.id.poster_field);
        posterField.setText(event.creatorID);

        typeField = (TextView)detailView.findViewById(R.id.type_field);
        typeField.setText(event.type.toString());

        dateField = (TextView)detailView.findViewById(R.id.date_field);
        dateField.setText(new SimpleDateFormat().format(event.date));

        addressField = (TextView)detailView.findViewById(R.id.address_field);
        addressField.setText(event.address);

        descriptionField = (TextView)detailView.findViewById(R.id.description_field);
        descriptionField.setText(event.description);

        String userID = (new Firebase(MainActivity.FIREBASE_URL)).getAuth().getUid();
        if (userID.equals(event.creatorID) && event.applicantIDs != null && !event.applicantIDs.isEmpty()) {
            viewResponses = (Button)detailView.findViewById(R.id.view_responses);

            viewResponses.setVisibility(View.VISIBLE);
            viewResponses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO display users who have applied for this shit
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Event details").setView(detailView).show();
    }
}

package com.example.sara.plusone.listeners;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sara.plusone.MainActivity;
import com.example.sara.plusone.R;
import com.example.sara.plusone.adapters.ApplicantsAdapter;
import com.example.sara.plusone.objects.Event;
import com.example.sara.plusone.objects.Person;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Map;

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

        posterField = (TextView)detailView.findViewById(R.id.poster_field);
        posterField.setText(event.creatorID);
        Firebase mFirebase = new Firebase(MainActivity.FIREBASE_URL).child("users");
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Map<String, String>> people = (Map<String,Map<String, String>>) dataSnapshot.getValue(Map.class);
                posterField.setText(people.get(event.creatorID).get("name"));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

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

                ListView listView;

                @Override
                public void onClick(View v) {
                    View responsesView = inflater.inflate(R.layout.fragment_submission_detail, null);

                    listView = (ListView)responsesView.findViewById(R.id.list_view);
                    listView.setAdapter(new ApplicantsAdapter(context, R.layout.applicant_detail, event.applicantIDs));

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Applicants").setView(responsesView).show();
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Event details").setView(detailView).show();
    }
}

package com.example.sara.plusone.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.sara.plusone.MainActivity;
import com.example.sara.plusone.R;
import com.example.sara.plusone.objects.Person;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Zack on 3/27/2016.
 */
public class ApplicantsAdapter extends ArrayAdapter<String> {

    private int resource;
    private Context context;
    private ArrayList<String> applicantIDs;
    private ArrayList<Person> applicants = new ArrayList<>();
    private Firebase mFirebase;

    public ApplicantsAdapter(Context context, int resource, final ArrayList<String> applicantIDs) {
        super(context, resource, applicantIDs);
        this.context = context;
        this.resource = resource;
        this.applicantIDs = applicantIDs;
        mFirebase = new Firebase(MainActivity.FIREBASE_URL).child("users");
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Map<String, String>> people = (Map<String,Map<String, String>>) dataSnapshot.getValue(Map.class);
                for (String uid : people.keySet()) {
                    if (applicantIDs.contains(uid)) {
                        Map<String, String> person = people.get(uid);
                        applicants.add(new Person(person.get("name"), Integer.parseInt(person.get("age")), null));
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            holder = new Holder();

            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.accept = (Button)convertView.findViewById(R.id.accept_button);
            holder.deny = (Button)convertView.findViewById(R.id.deny_button);

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }
        Person applicant = applicants.size() >= position ? null : applicants.get(position);
        holder.name.setText(applicant == null ? "Unknown" : applicant.getName() + ", " + applicant.getAge());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.accept.setText("Accepted");
                holder.accept.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
                holder.accept.setClickable(false);
                holder.deny.setText("Deny");
                holder.deny.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
                holder.deny.setClickable(false);
                //TODO notify the user that theyve been accepted
            }
        });

        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.deny.setText("Denied");
                holder.deny.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
                holder.deny.setClickable(false);
                holder.accept.setText("Accept");
                holder.accept.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
                holder.accept.setClickable(false);

            }
        });

        return convertView;
    }

    static class Holder {
        TextView name;
        Button accept;
        Button deny;
    }
}
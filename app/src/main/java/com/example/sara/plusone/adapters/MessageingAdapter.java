package com.example.sara.plusone.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sara.plusone.MainActivity;
import com.example.sara.plusone.R;
import com.example.sara.plusone.objects.Conversation;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by Shannor on 3/26/2016.
 */
public class MessageingAdapter extends FireBaseListAdapter<Conversation>{
    private String currentUser;
    private LayoutInflater mLayoutInflater;
    private Firebase mFireBaseRef;

    public MessageingAdapter(Query ref, Activity context, int layout, String currentUser){
        super(ref,Conversation.class,layout,context);
        this.currentUser = currentUser;
        mLayoutInflater = LayoutInflater.from(context);
        mFireBaseRef = new Firebase(MainActivity.FIREBASE_URL).child("users");
    }


    @Override
    protected void populateView(final View v, Conversation model) {
        String sender = model.getUid();
        //Pulls the user name from FireBase
        mFireBaseRef.child(sender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> userInfo = (Map<String, String>) dataSnapshot.getValue(Map.class);
                TextView senderName = (TextView) v.findViewById(R.id.sender_name);
                senderName.setText(userInfo.get("name"));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        ((TextView)v.findViewById(R.id.message_content)).setText(model.getMessage());

    }
}

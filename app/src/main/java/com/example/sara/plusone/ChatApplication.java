package com.example.sara.plusone;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.sara.plusone.adapters.ChatAdapter;
import com.example.sara.plusone.objects.Conversation;
import com.firebase.client.Firebase;

import java.util.List;

public class ChatApplication extends AppCompatActivity {

    ListView listView;
    ChatAdapter chatAdapter;
    EditText textToSend;
    Button sendMessage;
    Firebase mFirebaseRef;
    String otherPersonUID;
    String otherPersonName;
    String yourName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFirebaseRef = new Firebase(MainActivity.FIREBASE_URL).child("chat");
        otherPersonUID = getIntent().getStringExtra("uid");
        otherPersonName = getIntent().getStringExtra("name");
        yourName = getIntent().getStringExtra("currentName");
        listView = (ListView)findViewById(R.id.chat_list);
        sendMessage = (Button)findViewById(R.id.btnSend);
        textToSend  = (EditText)findViewById(R.id.sending_messages);
        chatAdapter = new ChatAdapter(mFirebaseRef.limitToLast(40),this,R.layout.chat_item,mFirebaseRef.getAuth().getUid(),
                otherPersonUID, yourName,otherPersonName);
        listView.setAdapter(chatAdapter);


        chatAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatAdapter.getCount() - 1);
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChat();
            }
        });

    }

    public void sendChat() {
        String input = textToSend.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Conversation conversation = new Conversation(input, mFirebaseRef.getAuth().getUid(), otherPersonUID);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(conversation);
            textToSend.setText("");
        }
    }
}

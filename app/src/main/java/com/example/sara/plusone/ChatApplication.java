package com.example.sara.plusone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.sara.plusone.adapters.ChatAdapter;

import java.util.List;

public class ChatApplication extends AppCompatActivity {

    ListView listView;
    ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String otherPersonUID = savedInstanceState.getString("uid");
        String otherPersonName = savedInstanceState.getString("name");

        listView = (ListView)findViewById(R.id.chat_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

}

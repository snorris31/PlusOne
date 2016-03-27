package com.example.sara.plusone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sara.plusone.objects.CurrentUser;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CreateAccount extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText age;
    Button create;
    Firebase mFirebase;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFirebase = new Firebase(MainActivity.FIREBASE_URL);

        email = (EditText)findViewById(R.id.editTextCreateEmail);
        password = (EditText)findViewById(R.id.editTextPassword);
        age = (EditText)findViewById(R.id.editTextAge);
        create = (Button)findViewById(R.id.buttonCreateAccount);
        name = (EditText)findViewById(R.id.editTextName);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final CountDownLatch latch = new CountDownLatch(1);
                mFirebase.createUser(email.getText().toString(), password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {
                        Log.d("Test", "Successfully created user account with uid: " + stringObjectMap.get("uid"));
//                        latch.countDown();
                        if (MainActivity.gUser == null) {
                            MainActivity.gUser = new CurrentUser(stringObjectMap.get("uid").toString(), name.getText().toString()
                                    , Integer.parseInt(age.getText().toString()), null);
                        }
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Log.d("Error", firebaseError.toString());
                    }
                });
//                awaitLatch(latch);
                finish();
            }
        });

    }

//    private void awaitLatch(CountDownLatch latch) {
//        try {
//            latch.await(, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}

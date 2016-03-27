package com.example.sara.plusone;

import android.content.Intent;
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
import com.example.sara.plusone.objects.SharedPrefWrapper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    Button signIn;
    Button makeAccount;
    EditText email;
    EditText password;
    Firebase mFirebase;
    SharedPrefWrapper mSharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(MainActivity.FIREBASE_URL);
        mSharePref = new SharedPrefWrapper(this);

        signIn = (Button)findViewById(R.id.signIn);
        makeAccount = (Button)findViewById(R.id.createAccount);
        email = (EditText)findViewById(R.id.editTextSignEmail);
        password = (EditText)findViewById(R.id.editSignInPassword);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CountDownLatch latch = new CountDownLatch(1);
                mFirebase.authWithPassword(email.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        String temp = authData.getUid();
                        latch.countDown();
                        Log.d("Test", "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                        Map<String,String> tempMap = new HashMap<String, String>();
                        tempMap.put("uid",temp);
                        tempMap.put("name","Shannor");
                        tempMap.put("age","21");
                        mFirebase.child("users").child(temp).setValue(tempMap);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {

                    }
                });
                awaitLatch(latch);
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        makeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),CreateAccount.class);
                startActivity(intent);
            }
        });
    }

        private void awaitLatch(CountDownLatch latch) {
        try {
            latch.await(700, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

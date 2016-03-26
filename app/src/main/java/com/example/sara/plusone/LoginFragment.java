package com.example.sara.plusone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sara.plusone.objects.CurrentUser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.callback.Callback;



public class LoginFragment extends Fragment {

    LoginButton loginButton;
    CallbackManager callbackManager;
    Firebase mFirebaseRef;
    AccessToken mAccessToken;
    int mAge ;
    String mName;
    Bitmap mProfilePic;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mFirebaseRef = new Firebase(MainActivity.FIREBASE_URL);
        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) view.findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                mAccessToken = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(mAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    mName = object.getString("name");
//                                    mProfilePic = getProfilePicture(object.getJSONObject("picture").getJSONObject("data").getString("url"));
                                    mAge =getAge(object.getString("birthday"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,birthday,picture");
                parameters.putBoolean("redirect", false);
                request.setParameters(parameters);
                request.executeAsync();

                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
                Log.d("Report","Logged in");
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.d("Report", "ERROR");
                // App code
            }
        });
        return view;
    }

    private int getAge(String birthDate){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy",Locale.US);
        int diff = 0;
        try {
            Date birth = sdf.parse(birthDate);
            Calendar currentDate = Calendar.getInstance();
            Date d1 = sdf.parse(sdf.format(currentDate.getTime()));//Returns 15/10/2012
            diff = (int) ((d1.getTime() - birth.getTime())/ (3.154e+10));
        }catch (ParseException e){
            e.printStackTrace();
        }


        return diff;
    }

    private Bitmap getProfilePicture(String pictureURL){
        Bitmap bitmap = null;
        try {
            URL facebookProfileURL = new URL("https://graph.facebook.com/"+ mAccessToken + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(facebookProfileURL.openConnection().getInputStream());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        onFacebookAccessTokenChange(mAccessToken);
    }

    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            mFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // The Facebook user is now authenticated with your Firebase app
                    Firebase userRef = new Firebase(MainActivity.FIREBASE_URL).child("users");
                    CurrentUser user = new CurrentUser(authData.getUid(),mName,mAge,null);
                    Map<String,CurrentUser> userMap = new HashMap<String, CurrentUser>();
                    userMap.put(authData.getUid(),user);
                    userRef.setValue(userMap);
               }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                }
            });
        } else {
        /* Logged out of Facebook so do a logout from the Firebase app */
            mFirebaseRef.unauth();
        }
    }
}

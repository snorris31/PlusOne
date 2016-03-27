package com.example.sara.plusone.objects;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.sara.plusone.LoginActivity;

/**
 * Created by Shannor on 3/26/2016.
 */
public class SharedPrefWrapper {
    //Shared Preferences
    SharedPreferences pref;

    //Used to edit information in the Share Preferences
    SharedPreferences.Editor editor;

    //Context of who is asking for it
    Context mContext;

    // Shared pref Mode
    private int PRIVATE_MODE = 0;


    //Share Pref, File Name
    private static final String PREF_NAME = "UserLoginInformation";


    //Save this, but Use Email to find user name with database
    public static final String KEY_NAME = "name";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_AGE = "age";

    public static final String KEY_UID = "uid";


    //Constructor
    public SharedPrefWrapper(Context context){
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Method to start the Login session and save the email.
     * @param email User's personal Email
     */
    public void createLoginSession(){
        editor.clear();
        editor.commit();
    }

    /**
     * Method to set the Current User name
     * MMakes sure that email has already been set
     * @param name current User Name
     */
    public void setName(String name){
        editor.putString(KEY_NAME,name);
        editor.commit();
    }

    public void setEmail(String email){
        editor.putString(KEY_EMAIL,email);
        editor.commit();
    }

    public void setAge(int age) {
        editor.putInt(KEY_AGE, age);
        editor.commit();
    }

    public void setUid(String uid){
        editor.putString(KEY_UID,uid);
        editor.commit();
    }

    /**
     * Clears the Users information,
     * then redirect them to the Login Activity.
     */

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
    /**
     * Gets the Users Email.
     * If expand in future can change to a HashMap and return more information,
     * about the User.
     * @return User Email or null.
     */
    public String getUserEmail(){
        return pref.getString(KEY_EMAIL, null);
    }

    public String getName() {
        return pref.getString(KEY_NAME, null);
    }

    public int getAge() {
        return pref.getInt(KEY_AGE, -1);
    }

    public String getUid() {
        return pref.getString(KEY_UID, null);
    }
}

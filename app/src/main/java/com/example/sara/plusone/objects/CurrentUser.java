package com.example.sara.plusone.objects;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by Zack on 3/26/2016.
 */
public class CurrentUser extends Person {

    public ArrayList<Integer> notifications;
    public String id;

    public CurrentUser(){

    }

    public CurrentUser(String id, String name, int age, Bitmap picture) {
        super(id,name, age, picture);
    }
}

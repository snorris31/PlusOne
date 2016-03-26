package com.example.sara.plusone.objects;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by Zack on 3/26/2016.
 */
public class CurrentUser extends Person {

    public ArrayList<Event> events;
    public ArrayList<Integer> notifications;

    public CurrentUser(String id, String name, int age, Drawable picture, ArrayList<Event> events, ArrayList<Integer> notifications) {
        super(id, name, age, picture);
        this.events = events;
        this.notifications = notifications;
    }
}

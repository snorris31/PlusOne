package com.example.sara.plusone.objects;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by Zack on 3/26/2016.
 */
public class CurrentUser extends Person {

    public ArrayList<Event> events;
    public ArrayList<Integer> notifications;
    private String id;

    public CurrentUser(String id, String name, int age, Bitmap picture) {
        super(id,name, age, picture);
    }
    public void setEvents(ArrayList<Event> e){
        this.events = e;
   }
    public String getUserID() {
        return this.id;
    }
}

package com.example.sara.plusone.objects;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zack on 3/25/2016.
 */

public class Person {

    public String id;
    public ArrayList<Event> eventList;
    public String name;
    public int age;
    public Drawable picture;

    public Person(String name, int age, Drawable picture) {
        this("-1", name, age, picture, null);
    }

    public Person(String id, String name, int age, Drawable picture, ArrayList<Event> eventList) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.picture = picture;
        this.eventList = eventList;
    }
}

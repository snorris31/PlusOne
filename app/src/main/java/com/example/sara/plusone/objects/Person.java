package com.example.sara.plusone.objects;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zack on 3/25/2016.
 */

public class Person {

    public String uid;
    public String name;
    public int age;

    @JsonIgnore
    public Bitmap picture;

    public Person(){

    }
    public Person(String name, int age, Bitmap picture) {
        this("-1", name, age, picture);
    }

    public Person(String uid, String name, int age, Bitmap picture) {
        this.uid = uid;
        this.name = name;
        this.age = age;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUid() {

        return uid;
    }
}

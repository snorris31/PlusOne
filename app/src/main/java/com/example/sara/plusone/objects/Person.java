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

    public String id;
    public String name;
    public int age;
    @JsonIgnore
    public Bitmap picture;

    public Person(){

    }
    public Person(String name, int age, Bitmap picture) {
        this("-1", name, age, picture);
    }

    public Person(String id, String name, int age, Bitmap picture) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.picture = picture;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

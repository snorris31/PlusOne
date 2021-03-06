package com.example.sara.plusone.objects;

import com.example.sara.plusone.enums.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Zack on 3/25/2016.
 */
public class Event{

    @JsonIgnore
    private long timestamp;

    public String creatorID;
    public ArrayList<String> applicantIDs;
    public String type;
    public Date date;
    public String address;
    public String title;
    public String time;
    public String description;
    public boolean completed;

    public Event(){
        //Default constructor for Firebase
    }

    public Event(String creatorID, ArrayList<String> applicantIDs, String type, Date date, String address, String time, String title, String description, boolean completed) {
        this.creatorID = creatorID;
        this.applicantIDs = applicantIDs == null ? new ArrayList<String>() : applicantIDs;
        this.type = type;
        this.date = date;
        this.address = address;
        this.title = title;
        this.time = time;
        this.timestamp = date.getTime();
        this.description = description == null ? "" : description;
        this.completed = completed;
    }


    public Date getDate(){
        return new Date(timestamp);
    }
    @JsonIgnore
    public long getTimestampLong(){
        return this.timestamp;
    }

    public void setTimestamp(long timeStamp){
        this.timestamp = timeStamp;
    }

    public ArrayList<String>getApplicantIDs(){
        return this.applicantIDs;
    }

    public String getCreatorID(){
        return this.creatorID;
    }
    public String getType(){
        return this.type;
    }

    public String getAddress(){
        return this.address;
    }

    public String getTitle(){
        return this.title;
    }
    public boolean isCompleted(){
        return this.completed;
    }

    public String getDescription(){
        return this.description;
    }

    public void setApplicantIDs(ArrayList<String> applicantIDs) {
        this.applicantIDs = applicantIDs;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setCreatorID(String creatorID) {

        this.creatorID = creatorID;
    }
}

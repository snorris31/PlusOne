package com.example.sara.plusone.objects;

import com.example.sara.plusone.enums.EventType;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Zack on 3/25/2016.
 */
public class Event {

    public String id;

    public String creatorID;
    public ArrayList<String> applicantIDs;

    public EventType type;
    public Date date;
    private long latitude;
    private long longitude;
    public String title;
    public String description;
    public boolean completed;

    public Event(String creatorID, ArrayList<String> applicantIDs, EventType type, Date date, long latitude, long longitude, String title, String description, boolean completed) {
        this("-1", creatorID, applicantIDs, type, date, latitude, longitude, title, description, completed);
    }

    public Event(String id, String creatorID, ArrayList<String> applicantIDs, EventType type, Date date, long latitude, long longitude, String title, String description, boolean completed) {
        this.id = id;
        this.creatorID = creatorID;
        this.applicantIDs = applicantIDs == null ? new ArrayList<String>() : applicantIDs;
        this.type = type;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.description = description == null ? "" : description;
        this.completed = completed;
    }
}

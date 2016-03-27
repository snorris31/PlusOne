package com.example.sara.plusone.objects;

/**
 * Created by Zack on 3/26/2016.
 */
public class Notification {
    public String affectedUserID;
    public String anySenderID;
    public String affectedEventID;
    public String body;

    public Notification(String affectedUserID, String anySenderID, String affectedEventID, String body) {
        this.affectedUserID = affectedUserID;
        this.anySenderID = anySenderID;
        this.affectedEventID = affectedEventID;
        this.body = body;
    }
}

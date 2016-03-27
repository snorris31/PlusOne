package com.example.sara.plusone.objects;

import java.util.Date;

/**
 * Created by Sara on 3/27/2016.
 */
public class Message {
    String currentID;
    String otherUserID;
    String message;
    Date date;
    String time;
    public Message(String currentID, String otherUserID, String message, Date date, String time) {
        this.currentID = currentID;
        this.otherUserID = otherUserID;
        this.message = message;
        this.date = date;
        this.time = time;
    }
}

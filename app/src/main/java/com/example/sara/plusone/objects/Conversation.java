package com.example.sara.plusone.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.Map;

/**
 * Created by Shannor on 3/26/2016.
 */
public class Conversation {
    private String message;
    private long timestamp;
    private String uid;

    @SuppressWarnings("unused")
    public Conversation(){

    }

    public Conversation(String msg, String uid){
        this.message = msg;
        this.uid = uid;
    }

    public Map<String,String> getTimestamp(){
        return ServerValue.TIMESTAMP;
    }

    @JsonIgnore
    public long getTimestampLong(){
        return this.timestamp;
    }
    public void setTimestamp(long timeStamp){
        this.timestamp = timeStamp;
    }
    public String getMessage(){
        return this.message;
    }

    public String getUid(){return this.uid;}

    public void setMessage(String message){
        this.message = message;
    }

    public void setUid(String uid){
        this.uid =  uid;
    }
}

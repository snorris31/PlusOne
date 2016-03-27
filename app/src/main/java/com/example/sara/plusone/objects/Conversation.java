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
    private String personalUid;
    private String recipentUid;

    @SuppressWarnings("unused")
    public Conversation(){

    }

    public Conversation(String msg, String uid, String recipentUid){
        this.message = msg;
        this.personalUid = uid;
        this.recipentUid = recipentUid;
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

    public String getPersonalUid(){return this.personalUid;}

    public String getRecipentUid() {
        return recipentUid;
    }

    public void setRecipentUid(String recipentUid) {
        this.recipentUid = recipentUid;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setPersonalUid(String uid){
        this.personalUid =  uid;
    }
}

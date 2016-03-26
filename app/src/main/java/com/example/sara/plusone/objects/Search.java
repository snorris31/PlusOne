package com.example.sara.plusone.objects;

/**
 * Created by Zack on 3/26/2016.
 */
public class Search {

    public String textMatch;
    public String eventType;

    //takes the format: "any", "nearMe", or "<lat>:<long>"
    public String latLong;

    public Search(String textMatch, String eventType, String latLong) {
        this.textMatch = textMatch;
        this.eventType = eventType;
        this.latLong = latLong;
    }
}

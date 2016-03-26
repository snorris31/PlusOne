package com.example.sara.plusone.enums;

import com.example.sara.plusone.R;

/**
 * Created by Zack on 3/25/2016.
 */
public enum EventType {

    //when adding new type, update below functions
    CONCERT, GREEK, PARTY, GYM, MOVIE, OTHER;

    public static int getColor(EventType type) {
        switch(type) {
            case CONCERT: return R.color.cyan;
            case GREEK: return R.color.yellow;
            case PARTY: return R.color.blue;
            case GYM: return R.color.red;
            case MOVIE: return R.color.green;
            case OTHER: return R.color.grey;
        }
        return 0;
    }

    @Override
    public String toString() {
        switch(this) {
            case CONCERT: return "Concert";
            case GREEK: return "Greek";
            case PARTY: return "Party";
            case GYM: return "Gym";
            case MOVIE: return "Movie";
            case OTHER: return "Other";
        }
        return null;
    }
}

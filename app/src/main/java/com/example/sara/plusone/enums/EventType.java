package com.example.sara.plusone.enums;

import com.example.sara.plusone.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Zack on 3/25/2016.
 */
public enum EventType {

    //when adding new type, update below functions
    FOOD, CONCERT, GREEK, PARTY, GYM, MOVIE, OTHER;

    public static ArrayList<String> asArrayList() {

        String[] array = {FOOD.toString(), CONCERT.toString(), GREEK.toString(), PARTY.toString(), GYM.toString(), MOVIE.toString(), OTHER.toString()};
        return new ArrayList<>(Arrays.asList(array));
    }

    public static int getColor(EventType type) {
        switch(type) {
            case FOOD: return R.color.purple;
            case CONCERT: return R.color.cyan;
            case GREEK: return R.color.yellow;
            case PARTY: return R.color.blue;
            case GYM: return R.color.red;
            case MOVIE: return R.color.green;
            case OTHER: return R.color.grey;
        }
        return 0;
    }

    public static EventType fromString(String s) {
        switch (s){
            case "Food": return FOOD;
            case "Concert": return CONCERT;
            case "Greek": return GREEK;
            case "Party": return PARTY;
            case "Gym": return GYM;
            case "Movie": return MOVIE;
            case "Other": return OTHER;
        }
        return null;
    }

    @Override
    public String toString() {
        switch(this) {
            case FOOD: return "Food";
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

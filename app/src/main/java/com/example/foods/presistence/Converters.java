package com.example.foods.presistence;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class Converters {

    // to take ingredient array and convert to String (Gson )
    @TypeConverter
    public static String fromArray(String [] strings){
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        return json;
    }

    // to back converted String to original array
    @TypeConverter
    public static String [] toArray(String s){
        Type type = new TypeToken<String[]>(){}.getType();
        return new Gson().fromJson(s , type);
    }
}

package com.iries.youtubealarm.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iries.youtubealarm.data.entity.alarm.DAY_OF_WEEK;

import java.util.Map;


public class MapTypeConverter {

    @TypeConverter
    public static Map<DAY_OF_WEEK, Integer> stringToMap(String json){
        return new Gson().fromJson(json,
                new TypeToken<Map<DAY_OF_WEEK, Integer>> () {});
    }

    @TypeConverter
    public static String mapToString(Map<DAY_OF_WEEK, Integer> daysId) {
       return daysId == null? "" : new Gson().toJson(daysId);
    }
}

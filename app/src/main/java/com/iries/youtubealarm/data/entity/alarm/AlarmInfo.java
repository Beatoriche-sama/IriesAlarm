package com.iries.youtubealarm.data.entity.alarm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.iries.youtubealarm.util.MapTypeConverter;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "ALARMS")
public class AlarmInfo {
    private int hour, minute;
    @PrimaryKey(autoGenerate = true)
    private long id;
    private boolean isActive;

    @TypeConverters(MapTypeConverter.class)
    private Map<DAY_OF_WEEK, Integer> daysId = new HashMap<>(7);

    public AlarmInfo(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public void setTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getFormattedTime() {
        return String.format("%02d:%02d", hour, minute);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setDaysId(Map<DAY_OF_WEEK, Integer> daysId) {
        this.daysId = daysId;
    }

    public Map<DAY_OF_WEEK, Integer> getDaysId() {
        return daysId;
    }
}

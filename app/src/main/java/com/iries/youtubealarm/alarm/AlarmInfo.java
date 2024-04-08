package com.iries.youtubealarm.alarm;

import java.util.HashMap;
import java.util.Map;

public class AlarmInfo {
    private int hour, minute;
    private int id;
    private boolean isActive;

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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }


    public Map<DAY_OF_WEEK, Integer> getDaysId() {
        return daysId;
    }
}

package com.iries.youtubealarm.data.entity.alarm;

import java.util.Arrays;

public enum DAY_OF_WEEK {
    SUNDAY(1, "Sunday"),
    MONDAY(2, "Monday"),
    TUESDAY(3, "Tuesday"),
    WEDNESDAY(4, "Wednesday"),
    THURSDAY(5, "Thursday"),
    FRIDAY(6,"Friday"),
    SATURDAY(7, "Saturday");

    private final int id;
    private final String name;

    DAY_OF_WEEK(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static DAY_OF_WEEK getById(int id){
        return Arrays.stream(DAY_OF_WEEK.values())
                .filter(day-> day.id == id)
                .findFirst().get();
    }
}

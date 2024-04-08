package com.iries.youtubealarm.youtube.filters;

import androidx.annotation.NonNull;

public enum DURATION{
    ANY("any"),
    SHORT("short"),
    MEDIUM("medium"),
    LONG("long");
    private final String durationName;

    DURATION(String durationName) {
        this.durationName = durationName;
    }

    public String getDurationName() {
        return durationName;
    }

    @NonNull
    @Override
    public String toString() {
        return durationName;
    }
}

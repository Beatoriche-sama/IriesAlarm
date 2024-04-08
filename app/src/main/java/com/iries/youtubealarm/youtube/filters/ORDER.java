package com.iries.youtubealarm.youtube.filters;

import androidx.annotation.NonNull;

public enum ORDER{
    DATE("date"),
    RATING("rating"),
    VIEW_COUNT("viewCount");
    private final String orderName;

    ORDER(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderName() {
        return orderName;
    }

    @NonNull
    @Override
    public String toString() {
        return orderName;
    }
}

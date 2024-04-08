package com.iries.youtubealarm.settings;

import com.iries.youtubealarm.youtube.filters.DURATION;
import com.iries.youtubealarm.youtube.filters.ORDER;

public class Settings {
    private DURATION duration;
    private ORDER order;

    public Settings(){
        this.duration = DURATION.ANY;
        this.order = ORDER.DATE;
    }
    public DURATION getDuration() {
        return duration;
    }

    public ORDER getOrder() {
        return order;
    }

    public void setDuration(DURATION duration) {
        this.duration = duration;
    }

    public void setOrder(ORDER order) {
        this.order = order;
    }

}

package com.iries.youtubealarm.data;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.iries.youtubealarm.alarm.AlarmInfo;
import com.iries.youtubealarm.settings.Settings;
import com.iries.youtubealarm.youtube.YTChannel;

import java.util.ArrayList;
import java.util.LinkedList;

public class RepositoryProvider {
    private DataRepository channelsRepo;
    private DataRepository alarmsRepo;
    private DataRepository settingsRepo;
    private final String channelsFileName
            = "data/data/com.iries.youtubealarm/files/app_data/yt_channels.json";
    private final String alarmsFileName
            = "data/data/com.iries.youtubealarm/files/app_data/alarms.json";
    private final String settingsFileName
            = "data/data/com.iries.youtubealarm/files/app_data/settings.json";

    private static RepositoryProvider instance;

    private RepositoryProvider() {
    }

    public void load() {
        channelsRepo = new DataRepository(
                new TypeToken<ArrayList<YTChannel>>() {
                },
                ArrayList::new) {
        };
        channelsRepo.loadData(channelsFileName);

        alarmsRepo = new DataRepository(
                new TypeToken<LinkedList<AlarmInfo>>() {
                },
                LinkedList::new);
        alarmsRepo.loadData(alarmsFileName);

        settingsRepo = new DataRepository(
                new TypeToken<Settings>() {
                }, Settings::new);
        settingsRepo.loadData(settingsFileName);
    }

    public void save() {
        channelsRepo.saveData(channelsFileName);
        alarmsRepo.saveData(alarmsFileName);
        settingsRepo.saveData(settingsFileName);
    }

    public static synchronized RepositoryProvider getInstance() {
        if (instance == null)
            instance = new RepositoryProvider();
        return instance;
    }

    public DataRepository getChannelsRepo() {
        return channelsRepo;
    }

    public DataRepository getAlarmsRepo() {
        return alarmsRepo;
    }

    public DataRepository getSettingsRepo() {
        return settingsRepo;
    }
}

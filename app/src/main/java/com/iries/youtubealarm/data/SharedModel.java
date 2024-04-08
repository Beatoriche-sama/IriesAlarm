package com.iries.youtubealarm.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.iries.youtubealarm.settings.Settings;
import com.iries.youtubealarm.alarm.AlarmInfo;
import com.iries.youtubealarm.youtube.YTChannel;

import java.util.ArrayList;
import java.util.LinkedList;

public class SharedModel extends AndroidViewModel {
    RepositoryProvider repositoryProvider;
    private final ArrayList<YTChannel> preferredYTChannels;
    private final LinkedList<AlarmInfo> alarms;
    private final Settings settings;

    public SharedModel(@NonNull Application application) {
        super(application);
        repositoryProvider = RepositoryProvider.getInstance();
        repositoryProvider.load();

        preferredYTChannels = (ArrayList<YTChannel>) repositoryProvider
                .getChannelsRepo().getObject();
        alarms = (LinkedList<AlarmInfo>) repositoryProvider
                .getAlarmsRepo().getObject();
        settings = (Settings) repositoryProvider.getSettingsRepo().getObject();
    }

    public void save(){
        repositoryProvider.save();
    }

    public ArrayList<YTChannel> getPreferredYTChannels() {
        return preferredYTChannels;
    }

    public LinkedList<AlarmInfo> getAlarms() {
        return alarms;
    }

    public Settings getSettings() {
        return settings;
    }

}

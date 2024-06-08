package com.iries.youtubealarm.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iries.youtubealarm.data.database.AlarmsRepo;
import com.iries.youtubealarm.data.database.ChannelsRepo;
import com.iries.youtubealarm.data.entity.alarm.AlarmInfo;
import com.iries.youtubealarm.data.entity.youtube.YTChannel;
import com.iries.youtubealarm.util.SettingsManager;

import java.util.List;

public class SharedModel extends AndroidViewModel {
    private final AlarmsRepo alarmsRepo;
    private final ChannelsRepo channelsRepo;
    private final LiveData<List<AlarmInfo>> allAlarms;
    private final LiveData<List<YTChannel>> allChannels;
    private final Settings settings;

    public SharedModel(@NonNull Application application) {
        super(application);

        alarmsRepo = new AlarmsRepo(application);
        channelsRepo = new ChannelsRepo(application);
        allAlarms = alarmsRepo.getAllAlarms();
        allChannels = channelsRepo.getAllChannels();
        settings = SettingsManager.load();
    }

    public void insert(YTChannel ytChannel) {
        channelsRepo.insert(ytChannel);
    }

    public void insert(AlarmInfo alarmInfo) {
        alarmsRepo.insert(alarmInfo);
    }

    public void update(AlarmInfo alarm) {
        alarmsRepo.update(alarm);
    }

    public void remove(YTChannel ytChannel) {
        channelsRepo.delete(ytChannel);
    }

    public void remove(AlarmInfo alarmInfo) {
        alarmsRepo.delete(alarmInfo);
    }

    public LiveData<List<YTChannel>> getAllChannels() {
        return allChannels;
    }

    public LiveData<List<AlarmInfo>> getAllAlarms() {
        return allAlarms;
    }

    public Settings getSettings() {
        return settings;
    }


}

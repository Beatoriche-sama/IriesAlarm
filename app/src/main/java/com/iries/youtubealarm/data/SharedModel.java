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
    //RepositoryProvider repositoryProvider;
    /*private final List<YTChannel> ytChannels;
    private final List<AlarmInfo> alarms;*/
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

        /*dbManager = DBManager.getInstance();
        dbManager.open(application);

        ytChannels = dbManager.fetchAllChannels();
        alarms = dbManager.fetchAllAlarms();
        settings = SettingsManager.load();*/

        /*repositoryProvider = RepositoryProvider.getInstance();
        repositoryProvider.load();

        preferredYTChannels = (ArrayList<YTChannel>) repositoryProvider
                .getChannelsRepo().getObject();
        alarms = (LinkedList<AlarmInfo>) repositoryProvider
                .getAlarmsRepo().getObject();
        settings = (Settings) repositoryProvider.getSettingsRepo().getObject();*/
    }

    public void insert(YTChannel ytChannel) {
        channelsRepo.insert(ytChannel);
        /*ytChannels.add(ytChannel);
        long id = dbManager.insert(ytChannel, DBHelper.CHANNELS_TABLE);*/
    }

    public void insert(AlarmInfo alarmInfo) {
        alarmsRepo.insert(alarmInfo);
        /*alarms.add(alarmInfo);
        long id = dbManager.insert(alarmInfo, DBHelper.ALARMS_TABLE);
        alarmInfo.setId(id);
        return id;*/
    }

    public void update(AlarmInfo alarm) {
        alarmsRepo.update(alarm);
    }

    public void remove(YTChannel ytChannel) {
        channelsRepo.delete(ytChannel);
        //dbManager.delete();
    }

    public void remove(AlarmInfo alarmInfo) {
        alarmsRepo.delete(alarmInfo);
        //dbManager.delete(alarmInfo.getId(), DBHelper.ALARMS_TABLE);
    }

    public void save() {
        //repositoryProvider.save();
       /* dbManager.updateTable(DBHelper.ALARMS_TABLE, alarms);
        dbManager.updateTable(DBHelper.CHANNELS_TABLE, ytChannels);
        SettingsManager.save(settings);*/
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

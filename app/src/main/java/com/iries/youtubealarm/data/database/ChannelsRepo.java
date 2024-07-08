package com.iries.youtubealarm.data.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.iries.youtubealarm.data.entity.youtube.YTChannel;

import java.util.List;

public class ChannelsRepo {
    private final ChannelsDao channelsDao;
    private final LiveData<List<YTChannel>> allChannels;

    public ChannelsRepo(Context context){
        Database database = Database.getInstance(context);
        channelsDao = database.channelsDao();
        allChannels = channelsDao.getAllChannels();
    }

    public void insert(YTChannel channel){
        channelsDao.insert(channel);
    }

    public void update(YTChannel channel){
        channelsDao.update(channel);
    }

    public void delete(YTChannel channel){
        channelsDao.delete(channel);
    }

    public void deleteAll(){
        channelsDao.deleteAll();
    }

    public LiveData<List<YTChannel>> getAllChannels(){
        return allChannels;
    }

    public int getChannelsCount(){
        return channelsDao.getChannelsCount();
    }

    public String getRandomChannelId(/*long dbId*/){
        return channelsDao.getChannelId(/*dbId*/);
    }
}

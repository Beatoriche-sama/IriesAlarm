package com.iries.youtubealarm.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.iries.youtubealarm.data.entity.youtube.YTChannel;

import java.util.List;

@Dao
public interface ChannelsDao {
    @Insert
    void insert(YTChannel ytChannel);

    @Delete
    void delete(YTChannel ytChannel);

    @Update
    void update(YTChannel ytChannel);

    @Query("DELETE FROM CHANNELS")
    void deleteAll();

    @Query("SELECT * FROM CHANNELS")
    LiveData<List<YTChannel>> getAllChannels();

    @Query("SELECT COUNT(id) FROM CHANNELS")
    int getChannelsCount();

    //@Query("SELECT channelId FROM CHANNELS WHERE id = :dbId")
    @Query("SELECT channelId FROM CHANNELS ORDER BY RANDOM() LIMIT 1")
    String getChannelId(/*long dbId*/);
}

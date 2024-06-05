package com.iries.youtubealarm.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.iries.youtubealarm.data.entity.alarm.AlarmInfo;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    void insert(AlarmInfo alarmInfo);

    @Delete
    void delete(AlarmInfo alarmInfo);

    @Update
    void update(AlarmInfo alarmInfo);

    @Query("DELETE FROM ALARMS")
    void deleteAll();

    @Query("SELECT * FROM ALARMS")
    LiveData<List<AlarmInfo>> getAllAlarms();
}

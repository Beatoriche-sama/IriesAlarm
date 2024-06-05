package com.iries.youtubealarm.data.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.iries.youtubealarm.data.entity.alarm.AlarmInfo;

import java.util.List;

public class AlarmsRepo {
    private final AlarmDao alarmDao;
    private final LiveData<List<AlarmInfo>> allAlarms;

    public AlarmsRepo(Context context){
        Database database = Database.getInstance(context);
        alarmDao = database.alarmDao();
        allAlarms = alarmDao.getAllAlarms();
    }

    public void insert(AlarmInfo alarmInfo){
        alarmDao.insert(alarmInfo);
    }

    public void update(AlarmInfo alarmInfo){
        alarmDao.update(alarmInfo);
    }

    public void delete(AlarmInfo alarmInfo){
        alarmDao.delete(alarmInfo);
    }

    public void deleteAll(){
        alarmDao.deleteAll();
    }

    public LiveData<List<AlarmInfo>> getAllAlarms(){
        return allAlarms;
    }
}

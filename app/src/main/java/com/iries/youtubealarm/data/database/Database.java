package com.iries.youtubealarm.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.iries.youtubealarm.data.entity.alarm.AlarmInfo;
import com.iries.youtubealarm.data.entity.youtube.YTChannel;

@androidx.room.Database(entities = {AlarmInfo.class, YTChannel.class}, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instance;
    private static final String DATABASE_NAME = "USER_DATA.DB";

    public abstract AlarmDao alarmDao();

    public abstract ChannelsDao channelsDao();

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            Builder<Database> builder = Room
                    .databaseBuilder(context, Database.class, DATABASE_NAME)
                    .allowMainThreadQueries();

            if (context.getDatabasePath(DATABASE_NAME).exists()) {
                builder.createFromAsset("databases/" + DATABASE_NAME);
            }

            instance = builder.build();

        }
        return instance;
    }

    @Override
    public void clearAllTables() {

    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration) {
        return null;
    }
}

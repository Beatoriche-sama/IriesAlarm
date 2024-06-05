package com.iries.youtubealarm.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.UI.activity.TriggeredAlarmActivity;
import com.iries.youtubealarm.UI.fragment.AlarmFragment;

public class NotificationService extends Service {

    private static final String CHANNEL_ID = "0";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String ringtoneName = intent
                .getStringExtra(RingtonePlayingService.RINGTONE_NAME_EXTRA);
        if (Settings.canDrawOverlays(this)) {
            Intent fullScreenIntent
                    = new Intent(this, TriggeredAlarmActivity.class);
            fullScreenIntent
                    .putExtra(RingtonePlayingService.RINGTONE_NAME_EXTRA, ringtoneName);
            fullScreenIntent
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(fullScreenIntent);
        } else createNotification(ringtoneName);

        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = getResources()
                .getString(R.string.app_name);
        String description = getResources()
                .getString(R.string.notification_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel
                = new NotificationChannel(CHANNEL_ID, name, importance);
        notificationChannel.setDescription(description);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager notificationManager
                = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }


    private void createNotification(String ringtoneName) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager
                    = getSystemService(NotificationManager.class);
            NotificationChannel existingChannel = mNotificationManager
                    .getNotificationChannel(CHANNEL_ID);
            if (existingChannel == null)
                createNotificationChannel();
        }

        Intent intent = new Intent(this, AlarmFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_access_alarm_24)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Now playing: " + ringtoneName)
                //getResources().getString(R.string.description)
                //.setFullScreenIntent(pendingIntent, true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManagerCompat notificationManager = NotificationManagerCompat
                .from(this);
        notificationManager.notify(Integer.parseInt(CHANNEL_ID), builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

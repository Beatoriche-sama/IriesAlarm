package com.iries.youtubealarm.service;


import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.google.api.services.youtube.YouTube;
import com.iries.youtubealarm.util.SettingsManager;
import com.iries.youtubealarm.data.database.ChannelsRepo;
import com.iries.youtubealarm.data.Settings;
import com.iries.youtubealarm.util.youtube.YoutubeAuth;
import com.iries.youtubealarm.util.youtube.YoutubeSearch;
import com.iries.youtubealarm.data.entity.youtube.Video;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.YoutubeDLRequest;
import com.yausername.youtubedl_android.mapper.VideoInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RingtonePlayingService extends Service {
    private Ringtone ringtone = null;
    public final static String RINGTONE_NAME_EXTRA = "ringtone_name_extra";
    private String chosenChannelId;
    private Settings settings;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ChannelsRepo channelsRepo = new ChannelsRepo(this);
        chosenChannelId = channelsRepo.getRandomChannelId();
        System.out.println("Chosen channel ID is: " + chosenChannelId);

        settings = SettingsManager.load();
        System.out.println("Duration is: " + settings.getDuration());

        startService();
        return START_STICKY;
    }

    private void startService() {
        YouTube youTube = YoutubeAuth.getYoutube(this);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            Video video = YoutubeSearch.findVideoByFilters(
                            youTube,
                            chosenChannelId,
                            settings.getOrder(),
                            settings.getDuration())
                    .get(0);
            handler.post(() -> playRingtone(video));
        });
        executor.shutdown();
    }

    private void startNotificationService(String ringtoneName) {
        Intent notificationIntent
                = new Intent(this, NotificationService.class);
        notificationIntent.putExtra(RINGTONE_NAME_EXTRA, ringtoneName);
        startService(notificationIntent);
    }

    private void playRingtone(Video video) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {

            Uri alarmUri;
            String ringtoneName;

            if (video == null) {
                alarmUri = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_ALARM);
                ringtoneName = "default ringtone";
            } else {
                alarmUri = extractAudio(
                        "https://youtu.be/" + video.getId());
                ringtoneName = video.getTitle();
            }

            handler.post(() -> {
                ringtone = RingtoneManager.getRingtone(this, alarmUri);
                ringtone.play();
                startNotificationService(ringtoneName);
            });

        });
        executor.shutdown();
    }

    private static Uri extractAudio(String videoURL) {
        YoutubeDLRequest request = new YoutubeDLRequest(videoURL);
        request.addOption("--extract-audio");
        VideoInfo streamInfo;
        try {
            streamInfo = YoutubeDL.getInstance().getInfo(request);
        } catch (YoutubeDLException | InterruptedException
                 | YoutubeDL.CanceledException e) {
            throw new RuntimeException(e);
        }
        return Uri.parse(streamInfo.getUrl());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Stop ringtone");
        shutUpRingtone();
    }

    private void shutUpRingtone() {
        if (ringtone != null)
            ringtone.stop();
        ringtone = null;
    }
}

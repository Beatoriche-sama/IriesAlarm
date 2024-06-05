package com.iries.youtubealarm.receiver;


import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iries.youtubealarm.UI.fragment.AlarmFragment;
import com.iries.youtubealarm.service.RingtonePlayingService;
import com.iries.youtubealarm.util.AlarmUtil;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRepeating = intent
                .getBooleanExtra(AlarmFragment.IS_REPEATING, false);
        if (isRepeating) {
            long timeInMillis = intent.getLongExtra(AlarmFragment.TIME_EXTRA, 0);
            int alarmId = intent.getIntExtra(AlarmFragment.ALARM_ID, 0);
            AlarmUtil.setAlarm(context, timeInMillis +
                    AlarmManager.INTERVAL_DAY * 7, alarmId, true);
        }

        Intent startIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(startIntent);
    }
}

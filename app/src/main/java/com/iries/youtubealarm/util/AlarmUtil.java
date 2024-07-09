package com.iries.youtubealarm.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.iries.youtubealarm.UI.fragment.AlarmFragment;
import com.iries.youtubealarm.data.entity.alarm.AlarmInfo;
import com.iries.youtubealarm.receiver.AlarmReceiver;
import com.iries.youtubealarm.data.entity.alarm.DAY_OF_WEEK;
import com.iries.youtubealarm.service.RingtonePlayingService;

import java.util.Calendar;

public class AlarmUtil {
    public static void setRepeatingAlarm(Context context,
                                         AlarmInfo alarm, DAY_OF_WEEK day) {
        int hour = alarm.getHour();
        int minute = alarm.getMinute();
        int chosenDay = day.getId();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.DAY_OF_WEEK, chosenDay);

        Calendar currentCalendar = Calendar.getInstance();
        if (calendar.before(currentCalendar)) {
            int currentDay = currentCalendar.get(Calendar.DAY_OF_WEEK);
            int diff =  7 - Math.abs(currentDay - chosenDay);
            calendar.add(Calendar.DATE, diff);
        }

        String fullAlarmIdString = String.valueOf(alarm.getId()) + (chosenDay);
        int fullAlarmId = Integer.parseInt(fullAlarmIdString);
        alarm.getDaysId().put(day, fullAlarmId);

        setAlarm(context, calendar.getTimeInMillis(), fullAlarmId, true);
    }

    public static void setAlarm(Context context,
                                long timeInMillis, int fullAlarmId,
                                boolean isRepeating) {
        int flags = isRepeating ?
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                : PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE;

        Intent nextAlarmIntent = new Intent(context, AlarmReceiver.class);
        nextAlarmIntent.putExtra(AlarmFragment.TIME_EXTRA, timeInMillis);
        nextAlarmIntent.putExtra(AlarmFragment.ALARM_ID, fullAlarmId);
        nextAlarmIntent.putExtra(AlarmFragment.IS_REPEATING, isRepeating);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, fullAlarmId, nextAlarmIntent, flags);

        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    public static void stopAlarm(Context context) {
        Intent stopIntent = new Intent(context, RingtonePlayingService.class);
        context.stopService(stopIntent);
    }

    public static void cancelIntent(int intentId, Context context) {
        int flags = PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE;
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, intentId, alarmIntent, flags);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}

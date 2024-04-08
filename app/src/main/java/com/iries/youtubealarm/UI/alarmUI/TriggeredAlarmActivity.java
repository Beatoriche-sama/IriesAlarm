package com.iries.youtubealarm.UI.alarmUI;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.UI.mainUI.MainActivity;
import com.iries.youtubealarm.alarm.service.RingtonePlayingService;
import com.iries.youtubealarm.util.AlarmUtil;

public class TriggeredAlarmActivity extends Activity {
    Context context;
    private static final int ADD_MILLIS = 180000; //3 min

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        context = getApplicationContext();
        setContentView(R.layout.triggered_alarm_activity);

        TextView textView = findViewById(R.id.overlay_textview);
        textView.setText("Now playing: " + savedInstanceState
                .getString(RingtonePlayingService.RINGTONE_NAME_EXTRA));

        Button snoozeButton = findViewById(R.id.snooze_button);
        snoozeButton.setOnClickListener(e -> snooze());
        Button dismissButton = findViewById(R.id.dismiss_button);
        dismissButton.setOnClickListener(e -> dismiss());

         /*PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wakeLock = powerManager
                .newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                        |PowerManager.ON_AFTER_RELEASE,"MyLock");
        if(!powerManager.isScreenOn()) {
            wakeLock.acquire(3*60*1000L); //keep CPU awake
        } else {
            wakeLock.release(); //disable keep CPU awake
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager
                    = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null)
                keyguardManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        super.onCreate(savedInstanceState);
    }

    private void snooze() {
        AlarmUtil.stopAlarm(context);
        System.out.println("Alarm Snoozed");
        //set new one shot alarm
        AlarmUtil.setAlarm(context,
                System.currentTimeMillis() + ADD_MILLIS, 0,
                false);
        toMainActivity();
    }

    private void dismiss() {
        AlarmUtil.stopAlarm(context);
        System.out.println("Alarm Dismissed");
        toMainActivity();
    }

    private void toMainActivity() {
        Intent in = new Intent(TriggeredAlarmActivity.this,
                MainActivity.class);
        startActivity(in);
        finish();
    }
}

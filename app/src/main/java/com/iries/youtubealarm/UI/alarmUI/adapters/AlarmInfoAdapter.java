package com.iries.youtubealarm.UI.alarmUI.adapters;

import static androidx.core.content.ContextCompat.getSystemService;

import static com.iries.youtubealarm.util.AlarmUtil.setRepeatingAlarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.UI.alarmUI.dialogs.EditAlarmDialog;
import com.iries.youtubealarm.alarm.AlarmInfo;
import com.iries.youtubealarm.alarm.service.RingtonePlayingService;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class AlarmInfoAdapter extends ArrayAdapter<AlarmInfo> {
    private final Context context;

    public AlarmInfoAdapter(@NonNull Context context, int resource,
                            LinkedList<AlarmInfo> alarms) {
        super(context, resource, alarms);
        this.context = context;
    }

    private static class AlarmHolder {
        TextView alarmTimeView;
        TextView activeDaysView;
    }

    @SuppressLint("AlarmHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        AlarmHolder holder;

        if (convertView == null) {
            LayoutInflater vi = getSystemService(getContext(),
                    LayoutInflater.class);
            assert vi != null;
            convertView = vi.inflate(R.layout.alarms_list_view, null);
            holder = new AlarmHolder();
            holder.alarmTimeView = convertView
                    .findViewById(R.id.alarm_time_view);
            holder.activeDaysView = convertView
                    .findViewById(R.id.alarm_active_days_view);
            convertView.setTag(holder);
        } else {
            holder = (AlarmHolder) convertView.getTag();
        }

        AlarmInfo alarm = getItem(position);
        String alarmTime = alarm.getFormattedTime();
        holder.alarmTimeView.setText(alarmTime);
        String activeDaysText = alarm.getDaysId()
                .keySet().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        holder.activeDaysView.setText(activeDaysText);

        Button button = convertView.findViewById(R.id.edit_time_button);
        button.setOnClickListener(e
                -> new EditAlarmDialog(alarm, context,this));
        ToggleButton toggleButton = convertView.findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(e
                -> onToggle(toggleButton.isChecked(), alarm));
        toggleButton.setChecked(alarm.isActive());
        return convertView;
    }

    public void onToggle(boolean isActive, AlarmInfo alarmInfo) {
        if (isActive) {
            Toast.makeText(context, "ALARM ON", Toast.LENGTH_SHORT).show();
            alarmInfo.getDaysId().keySet()
                    .forEach(day -> setRepeatingAlarm(context, alarmInfo, day));
            alarmInfo.setActive(true);
        } else {
            Intent stopIntent = new Intent(context, RingtonePlayingService.class);
            context.stopService(stopIntent);
            alarmInfo.setActive(false);
            Toast.makeText(context, "ALARM OFF", Toast.LENGTH_SHORT).show();
        }
    }


}

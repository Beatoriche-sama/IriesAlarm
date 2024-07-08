package com.iries.youtubealarm.UI.adapter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.data.entity.alarm.AlarmInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AlarmAdapter extends ArrayAdapter<AlarmInfo> {

    public AlarmAdapter(@NonNull Context context, int resource) {
        super(context, resource, new ArrayList<>());
    }

    public void setList(List<AlarmInfo> alarms) {
        clear();
        addAll(alarms);
        notifyDataSetChanged();
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

        Button editButton = convertView.findViewById(R.id.edit_time_button);
        editButton.setOnClickListener(e -> onEdit(alarm));

        ToggleButton toggleButton = convertView.findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(e
                -> onToggle(alarm.isActive(), alarm));
        toggleButton.setChecked(alarm.isActive());

        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(e-> onDelete(alarm));

        return convertView;
    }

    public abstract void onEdit(AlarmInfo alarmInfo);

    public abstract void onToggle(boolean isOn, AlarmInfo alarmInfo);

    public abstract void onDelete(AlarmInfo alarm);
}

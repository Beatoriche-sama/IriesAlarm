package com.iries.youtubealarm.UI.adapter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.data.entity.alarm.DAY_OF_WEEK;

import java.util.HashSet;
import java.util.Set;

public class WeekdaysAdapter extends ArrayAdapter<DAY_OF_WEEK> {
    private final Set<DAY_OF_WEEK> activeDaysList;
    public WeekdaysAdapter(@NonNull Context context, int resource,
                           Set<DAY_OF_WEEK> activeDays) {
        super(context, resource, DAY_OF_WEEK.values());
        activeDaysList = new HashSet<>(activeDays);
    }

    private static class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        WeekdaysAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = getSystemService(getContext(),
                    LayoutInflater.class);
            assert vi != null;
            convertView = vi.inflate(R.layout.weekdays_list_view, null);

            holder = new WeekdaysAdapter.ViewHolder();
            holder.textView = convertView.findViewById(R.id.weekday_code);
            holder.checkBox = convertView.findViewById(R.id.weekday_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (WeekdaysAdapter.ViewHolder) convertView.getTag();
        }

        DAY_OF_WEEK dayOfWeek = getItem(position);
        holder.checkBox.setText(dayOfWeek.getName());

        holder.checkBox.setChecked(activeDaysList.contains(dayOfWeek));
        holder.checkBox.setOnClickListener(e->{
            boolean isChecked = activeDaysList.contains(dayOfWeek);
            if (isChecked) activeDaysList.remove(dayOfWeek);
            else activeDaysList.add(dayOfWeek);
        });
        return convertView;
    }

    public Set<DAY_OF_WEEK> getActiveDaysList() {
        return activeDaysList;
    }
}

package com.iries.youtubealarm.UI.alarmUI.dialogs;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.iries.youtubealarm.util.AlarmUtil.cancelIntent;
import static com.iries.youtubealarm.util.AlarmUtil.setRepeatingAlarm;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TimePicker;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.UI.alarmUI.adapters.AlarmInfoAdapter;
import com.iries.youtubealarm.UI.alarmUI.adapters.WeekdaysAdapter;
import com.iries.youtubealarm.alarm.AlarmInfo;
import com.iries.youtubealarm.alarm.DAY_OF_WEEK;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

public class EditAlarmDialog extends AlertDialog {
    private final AlarmInfo alarm;
    private final Context context;
    private final AlarmInfoAdapter alarmInfoAdapter;
    private final DAY_OF_WEEK[] dayOfWeeks;

    public EditAlarmDialog(AlarmInfo alarm, Context context,
                           AlarmInfoAdapter alarmInfoAdapter) {
        super(context);
        this.alarm = alarm;
        this.context = context;
        this.alarmInfoAdapter = alarmInfoAdapter;
        this.dayOfWeeks = DAY_OF_WEEK.values();
        buildDialog().show();
    }

    public AlertDialog buildDialog() {
        Calendar calendar = Calendar.getInstance();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getSystemService(getContext(),
                LayoutInflater.class);
        View dialogView = inflater.inflate(R.layout.alarm_edit_dialog, null);
        dialogBuilder.setView(dialogView);

        TimePicker timePicker = dialogView
                .findViewById(R.id.alarm_edit_timepicker);
        ListView listView = dialogView.findViewById(R.id.alarm_edit_listview);
        WeekdaysAdapter weekdaysAdapter = new WeekdaysAdapter(context,
                R.layout.weekdays_list_view, dayOfWeeks, alarm.getDaysId().keySet());
        listView.setAdapter(weekdaysAdapter);

        dialogBuilder.setPositiveButton(android.R.string.ok,
                (dialog, which) -> onEditConfirm(timePicker,
                        weekdaysAdapter.getUpdatedDaysList()));
        dialogBuilder.setNegativeButton(android.R.string.cancel, null);
        dialogBuilder.setMessage("Set Time");

        AlertDialog alertDialog =  dialogBuilder.create();
        alertDialog.setOnShowListener(e -> {
            timePicker.setHour(calendar
                    .get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(calendar
                    .get(Calendar.MINUTE));
        });
        return alertDialog;
    }

    private void onEditConfirm(TimePicker timePicker, Set<DAY_OF_WEEK> updatedSet) {
        alarm.setTime(timePicker.getHour(),
                timePicker.getMinute());
        alarmInfoAdapter.notifyDataSetChanged();

        Map<DAY_OF_WEEK, Integer> originalDays = alarm.getDaysId();

        if (!originalDays.isEmpty()) {
            //cancel all pending intents
            originalDays.values().forEach(id -> cancelIntent(id, context));
            originalDays.clear();
            System.out.println("cancelling all intents");
        }
        if (!updatedSet.isEmpty()) {
            System.out.println("isn't empty");
            updatedSet.forEach(day -> setRepeatingAlarm(context, alarm, day));
        }
    }
}

package com.iries.youtubealarm.UI.alarmUI;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.UI.alarmUI.adapters.AlarmInfoAdapter;
import com.iries.youtubealarm.UI.alarmUI.dialogs.EditAlarmDialog;
import com.iries.youtubealarm.UI.alarmUI.dialogs.RingtoneSettingsDialog;
import com.iries.youtubealarm.alarm.AlarmInfo;
import com.iries.youtubealarm.databinding.AlarmFragmentBinding;

import com.iries.youtubealarm.data.SharedModel;
import com.iries.youtubealarm.settings.Settings;
import com.iries.youtubealarm.util.AlarmUtil;

import java.util.LinkedList;
import java.util.stream.IntStream;

public class AlarmFragment extends Fragment {

    private AlarmFragmentBinding binding;
    private Context context;
    private LinkedList<AlarmInfo> alarms;
    private Settings settings;
    private AlarmInfoAdapter alarmsListAdapter;
    public static final String TIME_EXTRA = "timeInMillis";
    public static final String ALARM_ID = "alarmId";
    public static final String IS_REPEATING = "is_repeating";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = requireActivity().getApplicationContext();
        SharedModel sharedModel
                = new ViewModelProvider(requireActivity()).get(SharedModel.class);
        alarms = sharedModel.getAlarms();
        settings = sharedModel.getSettings();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = AlarmFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = binding.alarmsView;
        alarmsListAdapter = new AlarmInfoAdapter(requireActivity(),
                R.layout.alarms_list_view, alarms);
        listView.setAdapter(alarmsListAdapter);

        binding.createAlarmButton.setOnClickListener(e -> createAlarm());

        binding.ringtoneSettingsButton.setOnClickListener(e
                -> new RingtoneSettingsDialog(requireActivity(), settings));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createAlarm() {
        AlarmInfo alarm = new AlarmInfo(0, 0);
        int size = alarms.size();
        //saving ids in case of removing older alarms
        if (size != 0 && alarms.contains(null)) {
            int index = IntStream.range(0, size)
                    .filter(i -> alarms.get(i) == null)
                    .findFirst().getAsInt();
            alarms.set(index, alarm);
        }
        else alarms.add(alarm);
        alarm.setId(alarms.indexOf(alarm) + 1);

        EditAlarmDialog editDialog = new EditAlarmDialog(alarm,
                requireActivity(), alarmsListAdapter);
        editDialog.setOnCancelListener(ee -> alarms.remove(alarm));
    }


    private void removeAlarm(AlarmInfo alarm) {
        alarm.getDaysId().values().forEach(intentId
                -> AlarmUtil.cancelIntent(intentId, context));
        alarms.set(alarms.indexOf(alarm), null);
    }
}
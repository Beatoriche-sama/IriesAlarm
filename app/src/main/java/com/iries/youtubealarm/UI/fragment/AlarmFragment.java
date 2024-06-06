package com.iries.youtubealarm.UI.fragment;

import static com.iries.youtubealarm.util.AlarmUtil.setRepeatingAlarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.UI.adapter.AlarmAdapter;
import com.iries.youtubealarm.UI.dialog.EditAlarmDialog;
import com.iries.youtubealarm.UI.dialog.SettingsDialog;
import com.iries.youtubealarm.data.entity.alarm.AlarmInfo;
import com.iries.youtubealarm.service.RingtonePlayingService;
import com.iries.youtubealarm.databinding.AlarmFragmentBinding;

import com.iries.youtubealarm.data.SharedModel;
import com.iries.youtubealarm.data.Settings;
import com.iries.youtubealarm.util.AlarmUtil;

public class AlarmFragment extends Fragment {
    private AlarmFragmentBinding binding;
    private Context context;
    private SharedModel sharedModel;
    private Settings settings;
    private AlarmAdapter alarmsListAdapter;
    public static final String TIME_EXTRA = "timeInMillis";
    public static final String ALARM_ID = "alarmId";
    public static final String IS_REPEATING = "is_repeating";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = requireActivity().getApplicationContext();
        this.sharedModel = new ViewModelProvider(
                requireActivity()).get(SharedModel.class);
        this.settings = sharedModel.getSettings();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = AlarmFragmentBinding.inflate
                (inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = binding.alarmsView;
        alarmsListAdapter = new AlarmAdapterImpl(
                requireActivity(), R.layout.alarms_list_view
        );
        listView.setAdapter(alarmsListAdapter);

        sharedModel.getAllAlarms()
                .observe(getViewLifecycleOwner(),
                        alarms -> alarmsListAdapter.setList(alarms));

        binding.createAlarmButton.setOnClickListener(e -> {
            AlarmInfo alarm = new AlarmInfo(0, 0);
            sharedModel.insert(alarm);
            new EditAlarmDialogImpl(alarm).setOnCancelListener
                    (ee -> sharedModel.remove(alarm));
        });

        binding.ringtoneSettingsButton.setOnClickListener(e
                -> new SettingsDialog(requireActivity(), settings));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class EditAlarmDialogImpl extends EditAlarmDialog {

        public EditAlarmDialogImpl(AlarmInfo alarm) {
            super(alarm, AlarmFragment.this.getContext());
        }

        @Override
        public void updateAlarmsList(AlarmInfo alarm) {
            sharedModel.update(alarm);
        }
    }

    private class AlarmAdapterImpl extends AlarmAdapter {

        public AlarmAdapterImpl(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public void onEdit(AlarmInfo alarmInfo) {
            new EditAlarmDialogImpl(alarmInfo);
        }

        @Override
        public void onToggle(boolean isOn, AlarmInfo alarmInfo) {
            if (isOn) {
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

        @Override
        public void onDelete(AlarmInfo alarm) {
            alarm.getDaysId().values().forEach(intentId
                    -> AlarmUtil.cancelIntent(intentId, context));
            sharedModel.remove(alarm);
        }
    }

}
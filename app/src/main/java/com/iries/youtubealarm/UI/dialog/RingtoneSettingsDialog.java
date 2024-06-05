package com.iries.youtubealarm.UI.dialog;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.data.Settings;
import com.iries.youtubealarm.data.entity.youtube.filters.DURATION;
import com.iries.youtubealarm.data.entity.youtube.filters.ORDER;

public class RingtoneSettingsDialog extends AlertDialog {
    private final Settings settings;
    private final Context context;

    public RingtoneSettingsDialog(Context context, Settings settings) {
        super(context);
        this.context = context;
        this.settings = settings;
        createDialog().show();
    }

    private AlertDialog createDialog() {
        LayoutInflater inflater = getSystemService(getContext(),
                LayoutInflater.class);
        View settingsView = inflater.inflate(R.layout.yt_prefs_settings, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(settingsView);

        ArrayAdapter<DURATION> durationsAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, DURATION.values());
        durationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner durationSpinner = settingsView.findViewById(R.id.filters_duration);
        durationSpinner.setAdapter(durationsAdapter);

        ArrayAdapter<ORDER> orderAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, ORDER.values());
        orderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner orderSpinner = settingsView.findViewById(R.id.filters_order);
        orderSpinner.setAdapter(orderAdapter);

        dialogBuilder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            settings.setOrder((ORDER) orderSpinner.getSelectedItem());
            settings.setDuration((DURATION)durationSpinner.getSelectedItem());

        });
        return dialogBuilder.create();
    }

}

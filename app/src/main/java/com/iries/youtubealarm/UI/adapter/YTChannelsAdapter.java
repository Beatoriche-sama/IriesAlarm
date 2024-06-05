package com.iries.youtubealarm.UI.adapter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iries.youtubealarm.R;
import com.iries.youtubealarm.data.entity.youtube.YTChannel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class YTChannelsAdapter extends ArrayAdapter<YTChannel> {
    private List<YTChannel> checkedChannels;

    public YTChannelsAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId, new ArrayList<>());
    }

    public void updateListView(List<YTChannel> currentList) {
        clear();
        addAll(currentList);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView,
                        @NonNull ViewGroup parent) {
        YTChannelsAdapter.ViewHolder holder;

        if (convertView == null) {
            LayoutInflater vi = getSystemService(getContext(),
                    LayoutInflater.class);
            assert vi != null;
            convertView = vi.inflate(R.layout.yt_channels_list_view, null);

            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.code);
            holder.checkBox = convertView.findViewById(R.id.yt_channel_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (YTChannelsAdapter.ViewHolder) convertView.getTag();
        }

        holder.checkBox.setOnClickListener(e -> {
            YTChannel channel = (YTChannel) e.getTag();
            boolean isChecked = checkedChannels.contains(channel);
            updateChannelsList(channel, isChecked);
        });

        YTChannel ytChannel = getItem(position);
        boolean isChosen = checkedChannels.contains(ytChannel);
        holder.checkBox.setChecked(isChosen);
        holder.checkBox.setText(ytChannel.getTitle());
        holder.checkBox.setTag(ytChannel);

        StrictMode.ThreadPolicy policy = new StrictMode
                .ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String source = ytChannel.getIconUrl();
        if (source != null) {
            Drawable drawable;
            try {
                drawable = getDrawableFromUrl(source);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            holder.checkBox.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable,
                    null, null, null);
        }
        return convertView;
    }

    public abstract void updateChannelsList(YTChannel channel, boolean isChecked);

    public void setCheckedChannels(List<YTChannel> checkedChannels) {
        this.checkedChannels = checkedChannels;
    }

    public List<YTChannel> getCheckedChannels() {
        return checkedChannels;
    }

    public Drawable getDrawableFromUrl(String link) throws java.io.IOException {
        URL url = new URL(link);
        Drawable drawable;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream input = new BufferedInputStream(urlConnection.getInputStream());
            drawable = new BitmapDrawable(Resources.getSystem(),
                    BitmapFactory.decodeStream(input));
        } finally {
            urlConnection.disconnect();
        }
        return drawable;
    }
}


package com.iries.youtubealarm.data.entity.youtube;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionSnippet;

import java.io.Serializable;

@Entity(tableName = "CHANNELS")
public class YTChannel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    String title;
    String channelId;
    String iconUrl;

    public YTChannel(SearchResult searchResult){
        SearchResultSnippet snippet = searchResult.getSnippet();
        this.title = snippet.getChannelTitle();
        this.channelId = snippet.getChannelId();
        this.iconUrl = snippet.getThumbnails().getDefault().getUrl();
    }

    public YTChannel(Subscription subscription){
        SubscriptionSnippet snippet = subscription.getSnippet();
        this.title = snippet.getTitle();
        this.channelId = snippet.getChannelId();
        this.iconUrl = snippet.getThumbnails().getDefault().getUrl();
    }


    public YTChannel(String title, String channelId) {
        this.title = title;
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getUploadsId() {
        return  channelId.substring(0, 1)
                + 'U' + channelId.substring(5);
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

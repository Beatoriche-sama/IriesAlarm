package com.iries.youtubealarm.util.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import com.iries.youtubealarm.data.entity.youtube.Video;
import com.iries.youtubealarm.data.entity.youtube.YTChannel;
import com.iries.youtubealarm.data.entity.youtube.filters.DURATION;
import com.iries.youtubealarm.data.entity.youtube.filters.ORDER;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class YoutubeSearch {

    public static ArrayList<YTChannel> getSubscriptions(YouTube youtube) {

        SubscriptionListResponse connectionsResponse;
        try {
            connectionsResponse = youtube
                    .subscriptions()
                    .list(Arrays.asList("snippet", "contentDetails"))
                    .setMine(true)
                    .setMaxResults(20L)
                    .execute();

            if (!connectionsResponse.isEmpty()) {
                ArrayList<YTChannel> userSubs = new ArrayList<>();
                connectionsResponse.getItems().forEach(sub
                        -> userSubs.add(new YTChannel(sub)));
                return userSubs;
            } else
                return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<YTChannel> findChannelByKeyword(YouTube youTube, String keyword) {
        try {
            SearchListResponse searchListResponse = youTube.search()
                    .list(Collections.singletonList("snippet"))
                    .setQ(keyword)
                    .setType(Collections.singletonList("channel")).execute();
            List<SearchResult> results = searchListResponse.getItems();

            if (!results.isEmpty()) {
                ArrayList<YTChannel> channels = new ArrayList<>();
                results.forEach(result
                        -> channels.add(new YTChannel(result)));
                return channels;
            } else
                return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Video> findVideoByFilters(YouTube youTube,
                                                 String channelId,
                                                 ORDER order, DURATION duration) {
        try {
            SearchListResponse searchListResponse = youTube.search()
                    .list(Collections.singletonList("snippet"))
                    .setChannelId(channelId)
                    .setOrder(order.getOrderName())
                    .setVideoDuration(duration.getDurationName())
                    .execute();

            List<SearchResult> results = searchListResponse.getItems();

            if (!results.isEmpty()) {
                List<Video> videoList = new ArrayList<>();
                results.forEach(result
                        -> videoList.add(new Video(result)));
                return videoList;
            } else
                return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

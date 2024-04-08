package com.iries.youtubealarm.youtube;


import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;

public class Video {
    String id;
    String title;

    public Video(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Video(SearchResult result) {
        SearchResultSnippet snippet = result.getSnippet();
        this.id = result.get("id").toString();
        this.title = snippet.getTitle();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
package com.iries.youtubealarm.data;

import android.content.Context;

public interface Repository {
    void saveData(String fullPath);

    void loadData(String fullPath);
}

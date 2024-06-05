package com.iries.youtubealarm.util;

import com.google.gson.Gson;
import com.iries.youtubealarm.data.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsManager {
    private static final String settingsFile
            = "data/data/com.iries.youtubealarm/files/app_data/settings.json";

    public static Settings load(){
        Settings settings;
        String json = "";
        try {
            File f = createFile();
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();

        if (json.isEmpty()) settings = new Settings();
        else settings = gson.fromJson(json, Settings.class);
        return settings;
    }

    public static void save(Settings settings){
        try {
            createFile();
            FileWriter fileWriter = new FileWriter(settingsFile);
            fileWriter.write(new Gson().toJson(settings));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File createFile() throws IOException {
        File file = new File(settingsFile);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }
}

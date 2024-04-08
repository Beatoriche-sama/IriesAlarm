package com.iries.youtubealarm.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;

public class JsonReader {

    private static File createFile(String fullPath) throws IOException {
        File file = new File(fullPath);
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }

    public static void save(String fullPath, Object object) {
        try {
            createFile(fullPath);
            FileWriter fileWriter = new FileWriter(fullPath);
            fileWriter.write(new Gson().toJson(object));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T deserialize(String fullPath,
                                    TypeToken<T> typeToken,
                                    Supplier<T> defaultVal) {
        String json = "";
        try {
            File f = createFile(fullPath);
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
        T object;

        if (json.isEmpty()) object = defaultVal.get();
         else object = gson.fromJson(json, typeToken.getType());
        return object;
    }
}

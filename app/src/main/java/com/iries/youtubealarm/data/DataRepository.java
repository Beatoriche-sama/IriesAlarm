package com.iries.youtubealarm.data;

import com.google.gson.reflect.TypeToken;
import com.iries.youtubealarm.util.JsonReader;

import java.util.function.Supplier;

public class DataRepository implements Repository {
    private final TypeToken type;
    private final Supplier supplier;
    private Object object;

    public DataRepository(TypeToken type, Supplier supplier) {
        this.type = type;
        this.supplier = supplier;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public void saveData(String fullPath) {
        if (object != null) JsonReader.save(fullPath, object);
    }

    @Override
    public void loadData(String fullPath) {
        object = JsonReader.deserialize(fullPath, type, supplier);
    }
}

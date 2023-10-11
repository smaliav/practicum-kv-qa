package ru.practicum.task_tracker.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.practicum.task_tracker.kv.KVClient;

public class DataManager {
    public static final String MAIN = "main";
    public static final String SIDE = "side";

    private final KVClient kvClient;
    private final Gson gson;
    private final Map<String, Data> mainDataByKey;
    private final Map<String, Data> sideDataByKey;

    public DataManager() {
        kvClient = new KVClient();
        gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        mainDataByKey = new HashMap<>();
        sideDataByKey = new HashMap<>();

        load();
    }

    public DataManager(KVClient kvClient, boolean isLoad) {
        this.kvClient = kvClient;
        gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        mainDataByKey = new HashMap<>();
        sideDataByKey = new HashMap<>();

        if (isLoad) {
            load();
        }
    }

    public Data getMainByKey(String key) {
        return mainDataByKey.get(key);
    }

    public Data getSideByKey(String key) {
        return sideDataByKey.get(key);
    }

    public void saveMainData(Data data) {
        mainDataByKey.put(data.getKey(), data);
        String json = new Gson().toJson(mainDataByKey.values());
        kvClient.put(MAIN, json);
    }

    public void saveSideData(Data data) {
        sideDataByKey.put(data.getKey(), data);
        String json = new Gson().toJson(sideDataByKey.values());
        kvClient.put(SIDE, json);
    }

    private void load() {
        String mainDataStr = kvClient.load(MAIN);
        if (mainDataStr != null) {
            List<Data> mainData = gson.fromJson(mainDataStr, new TypeToken<ArrayList<Data>>() {});
            mainData.forEach(d -> mainDataByKey.put(d.getKey(), d));
        }

        String sideDataStr = kvClient.load(SIDE);
        if (sideDataStr != null) {
            List<Data> sideData = gson.fromJson(sideDataStr, new TypeToken<ArrayList<Data>>() {});
            sideData.forEach(d -> sideDataByKey.put(d.getKey(), d));
        }
    }

    public static class Data {
        private final String key;
        private final String name;

        public Data(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Data data = (Data) o;
            return Objects.equals(key, data.key) && Objects.equals(name, data.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, name);
        }
    }
}

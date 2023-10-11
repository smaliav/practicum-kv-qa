package ru.practicum.task_tracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import ru.practicum.task_tracker.data.DataManager.Data;
import ru.practicum.task_tracker.kv.KVClient;

public class KVClientMock extends KVClient {

    Map<String, String> fakeStorage = new HashMap<>();

    @Override
    public String load(String key) {
        if ("main".equals(key)) {
            return new Gson().toJson(List.of(new Data("1", "Name")));
        }
        return null;
    }

    @Override
    public void put(String key, String value) {
        fakeStorage.put(key, value);
    }

    @Override
    protected String register() {
        return "DEBUG";
    }
}

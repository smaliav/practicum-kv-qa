package ru.practicum.task_tracker;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.task_tracker.data.DataManager;
import ru.practicum.task_tracker.data.DataManager.Data;
import ru.practicum.task_tracker.kv.KVServer;

public class HttpTaskManagerTest {

    KVServer kvServer;

    @BeforeEach
    public void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterEach
    public void shutDown() {
        kvServer.stop();
    }

    @Test
    public void testLoadFromHttpServer() {
        DataManager dataManager = new DataManager();
        Data initData = new Data("1", "Name");
        dataManager.saveMainData(initData);

        DataManager restoredManager = new DataManager();
        Data restoredData = restoredManager.getMainByKey("1");
        Assertions.assertEquals(initData, restoredData);
    }

    @Test
    public void testLoadFromHttpServerMocked() {
        DataManager dataManager = new DataManager(new KVClientMock(), false);
        Data initData = new Data("1", "Name");
        dataManager.saveMainData(initData);

        DataManager restoredManager = new DataManager(new KVClientMock(), true);
        Data restoredData = restoredManager.getMainByKey("1");
        Assertions.assertEquals(initData, restoredData);
    }
}

package ru.practicum.task_tracker;

import java.io.IOException;

import ru.practicum.task_tracker.data.DataServer;

public class DataServerStarter {
    public static void main(String[] args) throws IOException {
        DataServer dataServer = new DataServer();
        dataServer.start();
    }
}

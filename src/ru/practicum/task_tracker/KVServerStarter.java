package ru.practicum.task_tracker;

import java.io.IOException;

import ru.practicum.task_tracker.kv.KVServer;

public class KVServerStarter {
    public static void main(String[] args) throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
    }
}

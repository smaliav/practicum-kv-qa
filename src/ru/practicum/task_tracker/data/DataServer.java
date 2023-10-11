package ru.practicum.task_tracker.data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.task_tracker.data.DataManager.Data;

import static ru.practicum.task_tracker.data.DataManager.MAIN;
import static ru.practicum.task_tracker.data.DataManager.SIDE;

public class DataServer {
    public static final int PORT = 8080;

    private final HttpServer server;
    private final DataManager dataManager;

    /**
     * <ul>
     *     <li>/data/main?key=KEY - Главная</li>
     *     <li>/data/side?key=KEY - Второстепенная</li>
     * </ul>
     */
    public DataServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/data", this::handle);
        dataManager = new DataManager();
    }

    public void start() {
        System.out.println("Запускаем Data сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void handle(HttpExchange exchange) {
        try {
            String path;
            String key;
            Data data = null;

            switch (exchange.getRequestMethod()) {
                case "GET":
                    path = extractPath(exchange);
                    key = extractKey(exchange);

                    if (MAIN.equals(path)) {
                        data = dataManager.getMainByKey(key);
                    } else if (SIDE.equals(path)) {
                        data = dataManager.getSideByKey(key);
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                    }

                    sendData(exchange, data);
                    break;
                case "POST":
                    path = extractPath(exchange);
                    data = extractData(exchange);

                    if (MAIN.equals(path)) {
                        dataManager.saveMainData(data);
                    } else if (SIDE.equals(path)) {
                        dataManager.saveSideData(data);
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                        return;
                    }

                    exchange.sendResponseHeaders(201, 0);
                    break;
                default:
                    exchange.sendResponseHeaders(404, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private Data extractData(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return new Gson().fromJson(body, Data.class);
    }

    private String extractKey(HttpExchange exchange) {
        return exchange.getRequestURI().getQuery().substring(4);
    }

    private void sendData(HttpExchange exchange, Data data) throws IOException {
        if (data == null) {
            exchange.sendResponseHeaders(404, 0);
            return;
        }

        String json = new Gson().toJson(data);
        byte[] resp = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    private String extractPath(HttpExchange exchange) {
        return exchange.getRequestURI().getPath().substring(6);
    }
}

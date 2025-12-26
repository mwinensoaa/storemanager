package com.mwinensoaa.storemanager.network.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class ProductHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Only allow GET
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String response = """
            [
              {"id":1,"name":"Milk","price":12.50},
              {"id":2,"name":"Bread","price":8.00}
            ]
            """;

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }
}

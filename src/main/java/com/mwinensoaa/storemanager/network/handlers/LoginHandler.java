package com.mwinensoaa.storemanager.network.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // 1️⃣ Only allow POST
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        // 2️⃣ Read request body
        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        System.out.println("Login request: " + body);

        // 3️⃣ Validate credentials (dummy logic for now)
        boolean success = body.contains("\"username\":\"admin\"")
                && body.contains("\"password\":\"1234\"");

        String response;

        if (success) {
            response = """
                {
                  "status":"success",
                  "token":"abc123"
                }
                """;
            exchange.sendResponseHeaders(200, response.getBytes().length);
        } else {
            response = """
                {
                  "status":"error",
                  "message":"Invalid credentials"
                }
                """;
            exchange.sendResponseHeaders(401, response.getBytes().length);
        }

        // 4️⃣ Send response
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }
}


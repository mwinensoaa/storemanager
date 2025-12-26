package com.mwinensoaa.storemanager.network.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {


        private static String BASE_URL;
        private static final HttpClient client =
                HttpClient.newHttpClient();

        public static void init(String baseUrl) {
            BASE_URL = baseUrl;
        }

        public static String login(String json) throws Exception {
            return post("/login", json);
        }

        public static String getProducts() throws Exception {
            return get("/products");
        }

        public static String sendSale(String json) throws Exception {
            return post("/sales", json);
        }

        private static String get(String path) throws Exception {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .GET()
                    .build();

            return client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            ).body();
        }

        private static String post(String path, String json)
                throws Exception {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            ).body();
        }


}


package com.mwinensoaa.storemanager.network.server;

import com.mwinensoaa.storemanager.network.handlers.LoginHandler;
import com.mwinensoaa.storemanager.network.handlers.ProductHandler;
import com.mwinensoaa.storemanager.network.handlers.SalesHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class StoreServer {


    public static void initServerConnection(){
        HttpServer server = null;
        try {
            server = HttpServer.create(
                    new InetSocketAddress(8080), 0
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 🔗 Wire handlers to routes
        server.createContext("/login", new LoginHandler());
        server.createContext("/products", new ProductHandler());
        server.createContext("/sales", new SalesHandler());

        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        // 📡 Start broadcast discovery
        new Thread(new ServerBroadcaster()).start();

    }
}

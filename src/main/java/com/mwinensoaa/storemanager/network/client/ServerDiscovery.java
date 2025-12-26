package com.mwinensoaa.storemanager.network.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerDiscovery {

    private static final int PORT = 8888;

    public static String discoverServer() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            socket.setSoTimeout(10000); // wait max 10 seconds

            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            System.out.println("Listening for server broadcast...");

            socket.receive(packet);

            String message = new String(packet.getData(), 0, packet.getLength());

            if (message.startsWith("STORE_SERVER:")) {
                return message.split(":")[1];
            }

        } catch (Exception e) {
            System.out.println("Server not found");
        }
        return null;
    }
}


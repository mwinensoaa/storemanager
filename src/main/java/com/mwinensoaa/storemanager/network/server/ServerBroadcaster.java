package com.mwinensoaa.storemanager.network.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerBroadcaster implements Runnable {

    private static final int PORT = 8888;
    private static final int INTERVAL_MS = 3000; // every 3 seconds

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);

            String serverIp = InetAddress.getLocalHost().getHostAddress();
            String message = "STORE_SERVER:" + serverIp;

            byte[] data = message.getBytes();

            while (true) {
                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        InetAddress.getByName("255.255.255.255"),
                        PORT
                );
                socket.send(packet);
                System.out.println("Broadcasting server IP: " + serverIp);

                Thread.sleep(INTERVAL_MS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}


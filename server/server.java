import java.net.*;

import DatabaseManager.DatabaseManager;

import java.io.*;
import Service.Service;

public class server {
    public static void main(String[] args) {
        new ServerInstance(23333).start();
    }
}

class ServerInstance extends Thread {
    private final int port;

    ServerInstance(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket sock = ss.accept();
                System.out.println(sock.getInetAddress() + " connect");
                new Service(sock, databaseManager).start();
            }
        } catch (IOException ex) {
            System.out.println("Unable to bind to port " + port + "!");
        }
    }
}
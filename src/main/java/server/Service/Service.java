package server.Service;

import java.net.*;
import java.io.*;
import java.util.*;

import server.DatabaseManager.DatabaseManager;

public class Service extends Thread {
    private final Socket sock;
    private final DatabaseManager databaseManager;

    public Service(Socket sock, DatabaseManager databaseManager) {
        this.sock = sock;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {
        try {
            Scanner sin = new Scanner(sock.getInputStream());
            PrintStream sout = new PrintStream(sock.getOutputStream());

            String type = sin.nextLine(); // whether login or register
            String username = sin.nextLine();
            String password = sin.nextLine();

            System.out.println(type);
            System.out.println(username);
            System.out.println(password);

            AccountHandler accountHandler = new AccountHandler(databaseManager);

            if (type.equals("login")) {
                if (!accountHandler.login(username, password)) {
                    sout.println("500");
                    sout.println("Wrong username or password!");
                    sock.close();
                    return;
                }
            } else if (type.equals("register")) {
                System.out.println("register!");
                if (!accountHandler.register(username, password)) {
                    sout.println("500");
                    sout.println("Username already exists!");
                } else
                    sout.println("200");
                sock.close();
                return;
            } else {
                sout.println("500");
                sout.println("Unknown operation!");
                sock.close();
                return;
            }
            // after login
            sout.println("200");

        } catch (Exception e) {
            System.out.println(sock.getInetAddress() + "closed");
        }
    }
}

package Service;

import java.net.*;
import java.io.*;
import java.util.*;

import DatabaseManager.DatabaseManager;

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

            if (type == "login") {
                LoginHandler loginHandler = new LoginHandler(databaseManager);
                if (!loginHandler.login(username, password)) {
                    sout.println("500");
                    return;
                }
            } else if (type == "register") {
                RegisterHandler registerHandler = new RegisterHandler(databaseManager);
                if (!registerHandler.register(username, password))
                    sout.println("500");
                else
                    sout.println("200");
                return;
            } else {
                sout.println("500");
                return;
            }

        } catch (Exception e) {
            System.out.println(sock.getInetAddress() + "closed");
        }
    }
}

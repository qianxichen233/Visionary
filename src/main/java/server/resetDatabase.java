package server;

import server.DatabaseManager.DatabaseManager;

public class resetDatabase {
    public static void main(String[] args) {
        new DatabaseManager(true);
    }
}

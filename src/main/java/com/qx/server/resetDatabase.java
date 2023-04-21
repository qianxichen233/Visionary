package com.qx.server;

import com.qx.server.DatabaseManager.DatabaseManager;

public class resetDatabase {
    public static void main(String[] args) {
        new DatabaseManager(true);
    }
}

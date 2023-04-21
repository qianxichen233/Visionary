package com.qx.server.Service;

import com.qx.server.DatabaseManager.DatabaseManager;

import com.password4j.Hash;
import com.password4j.Password;

public class AccountHandler {
    private final DatabaseManager databaseManager;

    public AccountHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean login(String username, String password) {
        String pwd = databaseManager.getUserPassword(username);
        if (pwd == null)
            return false;
        return Password.check(password, pwd).withArgon2();
    }

    public boolean register(String username, String password) {
        Hash hash = Password.hash(password).withArgon2();
        return databaseManager.addUser(username, hash.getResult());
    }
}

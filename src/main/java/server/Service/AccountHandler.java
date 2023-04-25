package server.Service;

import java.util.UUID;

import com.password4j.Hash;
import com.password4j.Password;

import server.DatabaseManager.DatabaseManager;

public class AccountHandler {
    private final DatabaseManager databaseManager;

    public AccountHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean login(String username, String password) {
        String pwd = databaseManager.getUserPassword(username);
        if (pwd == null)
            return false;
        if (!Password.check(password, pwd).withArgon2())
            return false;
        return createSession(username);
    }

    public boolean register(String username, String password) {
        Hash hash = Password.hash(password).withArgon2();
        return databaseManager.addUser(username, hash.getResult());
    }

    public String getSession(String token) {
        return databaseManager.getSession(token);
    }

    public boolean logout(String username) {
        return clearSession(username);
    }

    private boolean createSession(String username) {
        String suuid = UUID.randomUUID().toString();
        return databaseManager.addSession(suuid, username);
    }

    private boolean clearSession(String username) {
        return databaseManager.clearSession(username);
    }
}

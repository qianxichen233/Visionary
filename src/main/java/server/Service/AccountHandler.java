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

    public String login(String username, String password) {
        String pwd = databaseManager.getUserPassword(username);
        if (pwd == null)
            return null;
        if (!Password.check(password, pwd).withArgon2())
            return null;
        String token = databaseManager.getSessionWithUsername(username);
        if (token == null)
            return createSession(username);
        return token;
    }

    public boolean register(String username, String password, byte[] salt) {
        Hash hash = Password.hash(password).withArgon2();
        return databaseManager.addUser(username, hash.getResult(), salt);
    }

    public String getSession(String token) {
        return databaseManager.getSessionWithToken(token);
    }

    public boolean logout(String username) {
        return clearSession(username);
    }

    private String createSession(String username) {
        String suuid = UUID.randomUUID().toString();
        if (!databaseManager.addSession(suuid, username, 30))
            return null;
        return suuid;
    }

    private boolean clearSession(String username) {
        return databaseManager.clearSession(username);
    }
}

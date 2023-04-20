package Service;

import DatabaseManager.DatabaseManager;

public class LoginHandler {
    private final DatabaseManager databaseManager;

    public LoginHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean login(String username, String password) {
        return true;
    }
}

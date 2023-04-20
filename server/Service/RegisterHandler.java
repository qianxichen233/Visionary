package Service;

import DatabaseManager.DatabaseManager;

public class RegisterHandler {
    private final DatabaseManager databaseManager;

    public RegisterHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public boolean register(String username, String password) {
        return true;
    }
}

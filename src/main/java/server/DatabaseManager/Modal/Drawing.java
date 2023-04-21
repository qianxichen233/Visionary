package server.DatabaseManager.Modal;

public class Drawing {
    public int ID;
    public String hash;
    public String username;
    public String filename;
    public String createdAt;

    public Drawing(int ID, String hash, String username, String filename, String createdAt) {
        this.ID = ID;
        this.hash = hash;
        this.username = username;
        this.filename = filename;
        this.createdAt = createdAt;
    }
}

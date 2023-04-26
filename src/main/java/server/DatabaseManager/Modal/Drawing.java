package server.DatabaseManager.Modal;

public class Drawing {
    public int ID;
    public String hash;
    public String username;
    public String filename;
    public String createdAt;
    public byte[] iv;
    public byte[] thumb_iv;

    public Drawing(int ID, String hash, String username, String filename, String createdAt, byte[] iv,
            byte[] thumb_iv) {
        this.ID = ID;
        this.hash = hash;
        this.username = username;
        this.filename = filename;
        this.createdAt = createdAt;
        this.iv = iv;
        this.thumb_iv = thumb_iv;
    }
}

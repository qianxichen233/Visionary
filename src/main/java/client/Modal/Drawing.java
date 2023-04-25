package client.Modal;

import java.awt.image.BufferedImage;

public class Drawing {
    public int ID;
    public String filename;
    public String createdAt;
    public BufferedImage thumb;

    public Drawing(int ID, String filename, String createdAt) {
        this.ID = ID;
        this.filename = filename;
        this.createdAt = createdAt;
    }

    public Drawing(int ID, String filename, String createdAt, BufferedImage thumb) {
        this.ID = ID;
        this.filename = filename;
        this.createdAt = createdAt;
        this.thumb = thumb;
    }
}

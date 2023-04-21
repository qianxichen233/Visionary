package client.Modal;

import java.awt.image.BufferedImage;

public class Drawing {
    public String filename;
    public String createdAt;
    public BufferedImage image;

    public Drawing(String filename, String createdAt) {
        this.filename = filename;
        this.createdAt = createdAt;
    }

    public Drawing(String filename, String createdAt, BufferedImage image) {
        this.filename = filename;
        this.createdAt = createdAt;
        this.image = image;
    }
}

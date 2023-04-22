package client.Canvas;

import java.awt.image.BufferedImage;

import client.utils.MyUtils;

public class Undoer {
    public static final int maxUndo = 16;

    private BufferedImage[] records = new BufferedImage[maxUndo];
    private int head;
    private int tail;

    public Undoer() {
        head = 0;
        tail = 0;
    }

    public void record(BufferedImage image) {
        records[head++] = MyUtils.deepCopy(image);

        if (head >= maxUndo)
            head = 0;
        if (head == tail) {
            ++tail;
            if (tail >= maxUndo)
                tail = 0;
        }
    }

    public BufferedImage undo() {
        if (head == tail)
            return null;
        --head;
        if (head < 0)
            head += maxUndo;
        return records[head];
    }
}

package client.Canvas;

import java.awt.image.BufferedImage;

import client.utils.MyUtils;

public class Undoer {
    public static final int maxUndo = 16;

    private Record[] records = new Record[maxUndo];
    private int rhead;
    private int head;
    private int tail;

    public static class Record {
        public BufferedImage mainCanvas;
        public BufferedImage savedCanvas;

        public Record(BufferedImage mainCanvas, BufferedImage savedCanvas) {
            this.mainCanvas = mainCanvas;
            this.savedCanvas = savedCanvas;
        }
    }

    public Undoer() {
        rhead = 0;
        head = 0;
        tail = 0;
    }

    public void record(BufferedImage mainCanvas, BufferedImage savedCanvas) {
        records[head++] = new Record(MyUtils.deepCopy(mainCanvas), MyUtils.deepCopy(savedCanvas));

        if (head >= maxUndo)
            head = 0;
        if (head == tail) {
            ++tail;
            if (tail >= maxUndo)
                tail = 0;
        }

        rhead = head;
    }

    public Record undo(BufferedImage mainCanvas, BufferedImage savedCanvas) {
        records[head] = new Record(MyUtils.deepCopy(mainCanvas), MyUtils.deepCopy(savedCanvas));
        if (head == tail)
            return null;
        --head;
        if (head < 0)
            head += maxUndo;
        return records[head];
    }

    public Record redo() {
        if (rhead == head)
            return null;
        ++head;
        if (head >= maxUndo)
            head = 0;
        return records[head];
    }
}

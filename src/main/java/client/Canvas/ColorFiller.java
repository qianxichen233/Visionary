package client.Canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.*;

public class ColorFiller {
    private static Point[] directions = { new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1) };

    private int toFill;

    public ColorFiller(int startX, int startY, BufferedImage img, String color) {
        Graphics g = img.getGraphics();
        g.setColor(Color.decode(color));
        toFill = img.getRGB(startX, startY);
        if (convertRGBHex(toFill) == color)
            return;

        ArrayDeque<Point> q = new ArrayDeque<Point>();
        Set<Long> visited = new HashSet<Long>();

        q.add(new Point(startX, startY));
        visited.add(hashPoint(startX, startY));

        while (!q.isEmpty()) {
            Point nxt = q.pop();
            g.drawLine(nxt.x, nxt.y, nxt.x, nxt.y);
            for (Point direction : directions) {
                int newX = nxt.x + direction.x;
                int newY = nxt.y + direction.y;

                if (newX < 0 || newX >= img.getWidth())
                    continue;
                if (newY < 0 || newY >= img.getHeight())
                    continue;
                if (visited.contains(hashPoint(newX, newY)))
                    continue;
                if (img.getRGB(newX, newY) != toFill)
                    continue;

                q.add(new Point(newX, newY));
                visited.add(hashPoint(newX, newY));
            }
        }
    }

    private static long hashPoint(int x, int y) {
        long A = (x >= 0 ? 2 * (long) x : -2 * (long) x - 1);
        long B = (y >= 0 ? 2 * (long) y : -2 * (long) y - 1);
        long C = ((A >= B ? A * A + A + B : A + B * B) / 2);
        return x < 0 && y < 0 || x >= 0 && y >= 0 ? C : -C - 1;
    }

    public static String convertRGBHex(int color) {
        int blue = color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red = (color & 0xff0000) >> 16;
        String result = "#" + String.format("%02X", red)
                + String.format("%02X", green)
                + String.format("%02X", blue);

        return result;
    }
}

package client.Canvas;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ShapeDrawer {
    public static class FormattedPoints {
        public Point topLeft;
        public Point bottomRight;
        public Point bottomLeft;
        public Point topRight;

        public FormattedPoints(int x1, int y1, int x2, int y2) {
            topLeft = new Point(Math.min(x1, x2), Math.min(y1, y2));
            bottomRight = new Point(Math.max(x1, x2), Math.max(y1, y2));
            bottomLeft = new Point(Math.min(x1, x2), Math.max(y1, y2));
            topRight = new Point(Math.max(x1, x2), Math.min(y1, y2));
        }

        public int getWidth() {
            return bottomRight.x - topLeft.x;
        }

        public int getHeight() {
            return bottomRight.y - topLeft.y;
        }

        public boolean inside(int x, int y) {
            return (x >= topLeft.x && x <= bottomRight.x) && (y >= topLeft.y && y <= bottomRight.y);
        }
    }

    public static void drawShape(String shape, int x1, int y1, int x2, int y2, int size, Graphics g) {
        if (shape.equals("Line")) {
            g.fillOval(x1 - size / 2, y1 - size / 2, size, size);
            g.fillOval(x2 - size / 2, y2 - size / 2, size, size);
            drawLine(x1, y1, x2, y2, size, g);
        } else if (shape.equals("Rectangle")) {
            drawRectangle(x1, y1, x2, y2, size, g);
        } else if (shape.equals("Circle")) {
            drawCircle(x1, y1, x2, y2, size, g);
        } else if (shape.equals("Triangle")) {
            drawTriangle(x1, y1, x2, y2, size, g);
        }
    }

    public static void drawLine(int x1, int y1, int x2, int y2, int size, Graphics g) {
        int Xdiff = x1 - x2;
        int Ydiff = y1 - y2;
        double dist = Math.sqrt(Ydiff * Ydiff + Xdiff * Xdiff);
        if (dist == 0)
            return;
        double angle = Math.acos(Xdiff / dist);
        angle = Ydiff < 0 ? -angle : angle;
        Rectangle rect = new Rectangle((int) Math.round(x2 + size * Math.sin(angle) / 2),
                (int) Math.round(y2 - size * Math.cos(angle) / 2), (int) Math.round(dist), size);

        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        g2d.rotate(angle, rect.x, rect.y);
        g2d.draw(rect);
        g2d.fill(rect);
        g2d.setTransform(old);
    }

    public static void drawRectangle(int x1, int y1, int x2, int y2, int size, Graphics g) {
        FormattedPoints formattedPoints = new FormattedPoints(x1, y1, x2, y2);

        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(size));
        g2d.drawRect(formattedPoints.topLeft.x, formattedPoints.topLeft.y, formattedPoints.getWidth(),
                formattedPoints.getHeight());
        g2d.setStroke(oldStroke);
    }

    public static void drawRectangle(int x1, int y1, int x2, int y2, Graphics g, Stroke stroke) {
        FormattedPoints formattedPoints = new FormattedPoints(x1, y1, x2, y2);

        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(stroke);
        g2d.drawRect(formattedPoints.topLeft.x, formattedPoints.topLeft.y, formattedPoints.getWidth(),
                formattedPoints.getHeight());
        g2d.setStroke(oldStroke);
    }

    public static void drawCircle(int x1, int y1, int x2, int y2, int size, Graphics g) {
        FormattedPoints formattedPoints = new FormattedPoints(x1, y1, x2, y2);

        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(size));
        g2d.drawOval(formattedPoints.topLeft.x, formattedPoints.topLeft.y, formattedPoints.getWidth(),
                formattedPoints.getHeight());
        g2d.setStroke(oldStroke);
    }

    public static void drawTriangle(int x1, int y1, int x2, int y2, int size, Graphics g) {
        FormattedPoints formattedPoints = new FormattedPoints(x1, y1, x2, y2);

        int x[] = { formattedPoints.topLeft.x, formattedPoints.bottomLeft.x, formattedPoints.bottomRight.x };
        int y[] = { formattedPoints.topLeft.y, formattedPoints.bottomLeft.y, formattedPoints.bottomRight.y };

        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(size));
        g2d.drawPolygon(x, y, 3);
        g2d.setStroke(oldStroke);
    }
}

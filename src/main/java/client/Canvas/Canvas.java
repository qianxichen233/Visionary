package client.Canvas;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import client.Panel.DrawingPanel;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener {
    private static final int width = 800;
    private static final int height = 700;
    private static final int padding = 30;
    public static final String defaultMainColor = "#000000";
    public static final String defaultSecondaryColor = "#FFFFFF";
    public static final int defaultSize = 5;

    private BufferedImage bufferedImage;

    private volatile String mainColor = defaultMainColor;
    private volatile String secondaryColor = defaultSecondaryColor;
    private volatile int size = defaultSize;

    private DrawingPanel panel;

    public Canvas(DrawingPanel panel) {
        super();
        this.panel = panel;
        addMouseListener(this);
        addMouseMotionListener(this);
        setBounds(padding, padding, width - 3 * padding, height - 3 * padding);
        setLayout(null);
        setBackground(Color.white);
        bufferedImage = new BufferedImage(
                getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = bufferedImage.createGraphics();
        ig2.setBackground(Color.WHITE);
        ig2.clearRect(0, 0, getWidth(), getHeight());
    }

    public Canvas(DrawingPanel panel, BufferedImage bufferedImage) {
        super();
        this.panel = panel;
        addMouseListener(this);
        addMouseMotionListener(this);
        setBounds(padding, padding, width - 3 * padding, height - 3 * padding);
        setLayout(null);
        setBackground(Color.white);
        this.bufferedImage = bufferedImage;
    }

    public void setMainColor(String color) {
        this.mainColor = color;
    }

    public void setSecondaryColor(String color) {
        this.secondaryColor = color;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private void draw(int x, int y, String color) {
        Graphics g = bufferedImage.getGraphics();
        g.setColor(Color.decode(color));
        g.fillOval(x - (size / 2), y - (size / 2), size, size);
        repaint();
    }

    public void saveAsImage(String path) {
        panel.saveAsImage(bufferedImage, path);
    }

    public void saveAsImageRemote() {
        panel.saveAsImageRemote(bufferedImage);
    }

    public void loadImage(String path) {
        try {
            bufferedImage = ImageIO.read(new File(path));
            repaint();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, null);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e))
            draw(e.getX(), e.getY(), secondaryColor);
        else
            draw(e.getX(), e.getY(), mainColor);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e))
            draw(e.getX(), e.getY(), secondaryColor);
        else
            draw(e.getX(), e.getY(), mainColor);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
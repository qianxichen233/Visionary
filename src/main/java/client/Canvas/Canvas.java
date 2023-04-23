package client.Canvas;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.Panel.DrawingPanel;
import client.utils.MyUtils;

import java.awt.image.BufferedImage;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener {
    private static final int width = 800;
    private static final int height = 700;
    private static final int padding = 30;
    public static final String defaultMainColor = "#000000";
    public static final String defaultSecondaryColor = "#FFFFFF";
    public static final int defaultSize = 5;

    public static final int mode_pen = 0;
    public static final int mode_shape = 1;
    public static final int mode_erase = 2;
    public static final int mode_pick = 3;
    public static final int mode_fill = 4;

    private BufferedImage mainCanvas;
    private BufferedImage savedCanvas;

    private volatile String mainColor = defaultMainColor;
    private volatile String secondaryColor = defaultSecondaryColor;
    private volatile int size = defaultSize;

    private DrawingPanel panel;
    private Undoer undoer = new Undoer();

    private int mode = mode_pen;

    // used for pen mode
    private int prevX = -1;
    private int prevY = -1;

    // used for shape mode
    private String shape;
    private int startX = -1;
    private int startY = -1;

    private Canvas() {
        super();
        addMouseListener(this);
        addMouseMotionListener(this);
        setBounds(padding, padding, width - 3 * padding, height - 3 * padding);
        setLayout(null);
        setBackground(Color.white);
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // super.mouseReleased(e);
                Canvas.this.grabFocus();
            }
        });

        getActionMap().put("undo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "undo");
    }

    public Canvas(DrawingPanel panel) {
        this();
        this.panel = panel;
        mainCanvas = new BufferedImage(
                getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = mainCanvas.createGraphics();
        ig2.setBackground(Color.WHITE);
        ig2.clearRect(0, 0, getWidth(), getHeight());
    }

    public Canvas(DrawingPanel panel, BufferedImage bufferedImage) {
        this();
        this.panel = panel;
        this.mainCanvas = bufferedImage;
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

    private void penDraw(int x, int y, String color) {
        Graphics g = mainCanvas.getGraphics();
        g.setColor(Color.decode(color));
        g.fillOval(x - size / 2, y - size / 2, size, size);
        autoFill(x, y, g);

        prevX = x;
        prevY = y;

        repaint();
    }

    private void drawShape(int curX, int curY, String color) {
        mainCanvas = MyUtils.deepCopy(savedCanvas);
        Graphics g = mainCanvas.getGraphics();
        g.setColor(Color.decode(color));
        ShapeDrawer.drawShape(shape, startX, startY, curX, curY, size, g);

        repaint();
    }

    private void autoFill(int x, int y, Graphics g) {
        if (prevX == -1 || prevY == -1)
            return;

        ShapeDrawer.drawLine(x, y, prevX, prevY, size, g);
    }

    public void saveAsImage(String path) {
        panel.saveAsImage(mainCanvas, path);
    }

    public void saveAsImageRemote() {
        panel.saveAsImageRemote(mainCanvas);
    }

    public void undo() {
        BufferedImage previous = undoer.undo();
        if (previous == null)
            return;
        mainCanvas = previous;
        repaint();
    }

    public void pickColor(int x, int y, boolean isMainColor) {
        int clr = mainCanvas.getRGB(x, y);
        int blue = clr & 0xff;
        int green = (clr & 0xff00) >> 8;
        int red = (clr & 0xff0000) >> 16;
        String color = "#" + String.format("%02X", red)
                + String.format("%02X", green)
                + String.format("%02X", blue);
        if (isMainColor)
            panel.toolbar.onMainColorChange(color);
        else
            panel.toolbar.onSecondaryColorChange(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(mainCanvas, 0, 0, null);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mode == Canvas.mode_pen) {
            if (SwingUtilities.isRightMouseButton(e))
                penDraw(e.getX(), e.getY(), secondaryColor);
            else
                penDraw(e.getX(), e.getY(), mainColor);
        } else if (mode == Canvas.mode_shape) {
            if (SwingUtilities.isRightMouseButton(e))
                drawShape(e.getX(), e.getY(), secondaryColor);
            else
                drawShape(e.getX(), e.getY(), mainColor);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (mode == Canvas.mode_pen) {
            if (SwingUtilities.isRightMouseButton(e))
                penDraw(e.getX(), e.getY(), secondaryColor);
            else
                penDraw(e.getX(), e.getY(), mainColor);
            prevX = -1;
            prevY = -1;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (mode == Canvas.mode_pen) {
            undoer.record(mainCanvas);
        }
        if (mode == Canvas.mode_shape) {
            startX = e.getX();
            startY = e.getY();
            savedCanvas = MyUtils.deepCopy(mainCanvas);
            undoer.record(mainCanvas);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (mode == Canvas.mode_pen) {
            prevX = -1;
            prevY = -1;
        } else if (mode == Canvas.mode_shape) {
            savedCanvas = null;
        } else if (mode == Canvas.mode_pick) {
            pickColor(e.getX(), e.getY(), SwingUtilities.isLeftMouseButton(e));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (mode == Canvas.mode_pen) {
            prevX = -1;
            prevY = -1;
        }
    }

    public void setMode(int mode, String... args) {
        this.mode = mode;
        if (mode == Canvas.mode_shape) {
            shape = args[0];
        }
    }
}
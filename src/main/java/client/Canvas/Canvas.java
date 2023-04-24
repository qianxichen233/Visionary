package client.Canvas;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.Canvas.ShapeDrawer.FormattedPoints;
import client.Panel.DrawingPanel;
import client.utils.*;

import java.awt.image.BufferedImage;

public class Canvas extends JPanel implements MouseListener, MouseMotionListener {
    // panel layout
    private static final int width = 800;
    private static final int height = 700;
    private static final int padding = 30;

    // canvas default values
    public static final Color backgroundColor = Color.WHITE;
    public static final String defaultMainColor = "#000000";
    public static final String defaultSecondaryColor = "#FFFFFF";
    public static final int defaultSize = 5;

    // canvas modes
    public static final int mode_pen = 0;
    public static final int mode_shape = 1;
    public static final int mode_erase = 2;
    public static final int mode_pick = 3;
    public static final int mode_fill = 4;
    public static final int mode_select = 5;

    // canvas
    private BufferedImage mainCanvas;
    private BufferedImage savedCanvas;

    // current status
    private volatile String mainColor = defaultMainColor;
    private volatile String secondaryColor = defaultSecondaryColor;
    private volatile int size = defaultSize;
    private volatile int mode = mode_select;

    // parent panel
    private DrawingPanel panel;

    // undo controller
    private Undoer undoer = new Undoer();

    // used for pen mode
    private int prevX = -1;
    private int prevY = -1;

    // used for shape mode
    private String shape;
    private int startX = -1;
    private int startY = -1;

    // used for select mode
    private static final int submode_move = 0;
    private static final int submode_select = 1;

    private boolean selected = false;
    private Point selectSt;
    private Point selectEd;
    private BufferedImage clip;

    private int select_submode;
    private Point moveSt;
    private Point initialSelectSt;
    private Point initialSelectEd;

    private Canvas() {
        super();
        addMouseListener(this);
        addMouseMotionListener(this);
        setBounds(padding, padding, width - 3 * padding, height - 3 * padding);
        setLayout(null);
        setBackground(backgroundColor);
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
        getActionMap().put("redo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });
        getActionMap().put("copy", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                saveToClipBoard();
            }
        });
        getActionMap().put("paste", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                loadFromClipBoard();
            }
        });

        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "undo");

        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | KeyEvent.SHIFT_DOWN_MASK),
                "redo");

        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "copy");

        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "paste");
    }

    public Canvas(DrawingPanel panel) {
        this();
        this.panel = panel;
        mainCanvas = new BufferedImage(
                getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = mainCanvas.createGraphics();
        ig2.setBackground(backgroundColor);
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

    private void drawSelectedArea(int curX, int curY, boolean drawClip) {
        mainCanvas = MyUtils.deepCopy(savedCanvas);
        Graphics g = mainCanvas.getGraphics();
        if (drawClip)
            g.drawImage(clip, selectSt.x, selectSt.y, null);
        g.setColor(Color.gray);
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[] { 9 }, 0);
        ShapeDrawer.drawRectangle(selectSt.x, selectSt.y, curX, curY, g, dashed);

        repaint();
    }

    private void moveSelectedArea(int curX, int curY) {
        mainCanvas = MyUtils.deepCopy(savedCanvas);
        Graphics g = mainCanvas.getGraphics();
        Point offset = new Point(curX - moveSt.x, curY - moveSt.y);

        // clear initial area
        if (initialSelectSt != null) {
            g.setColor(backgroundColor);
            FormattedPoints formatted = new FormattedPoints(initialSelectSt.x, initialSelectSt.y, initialSelectEd.x,
                    initialSelectEd.y);
            g.fillRect(formatted.topLeft.x, formatted.topLeft.y, formatted.getWidth(), formatted.getHeight());
        }

        // draw moved area
        g.drawImage(clip, selectSt.x + offset.x, selectSt.y + offset.y, null);

        // draw border
        g.setColor(Color.gray);
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[] { 9 }, 0);
        ShapeDrawer.drawRectangle(selectSt.x + offset.x, selectSt.y + offset.y, selectEd.x + offset.x,
                selectEd.y + offset.y, g, dashed);

        repaint();
    }

    private void applySelectedArea() {
        mainCanvas = MyUtils.deepCopy(savedCanvas);
        Graphics g = mainCanvas.getGraphics();
        if (initialSelectSt != null) {
            g.setColor(backgroundColor);
            FormattedPoints formatted = new FormattedPoints(initialSelectSt.x, initialSelectSt.y, initialSelectEd.x,
                    initialSelectEd.y);
            g.fillRect(formatted.topLeft.x, formatted.topLeft.y, formatted.getWidth(), formatted.getHeight());
        }
        g.drawImage(clip, selectSt.x, selectSt.y, null);
        savedCanvas = null;
        repaint();
    }

    private void autoFill(int x, int y, Graphics g) {
        if (prevX == -1 || prevY == -1)
            return;

        ShapeDrawer.drawLine(x, y, prevX, prevY, size, g);
    }

    private void saveToClipBoard() {
        if (!selected)
            return;
        new CopyImagetoClipBoard().copyImage(clip);
    }

    public void loadFromClipBoard() {
        BufferedImage img = CopyImagetoClipBoard.pasteImageFromClipboard();
        if (img == null)
            return;

        setMode(Canvas.mode_select);
        clip = img;
        savedCanvas = MyUtils.deepCopy(mainCanvas);
        selectSt = new Point(0, 0);
        selectEd = new Point(img.getWidth(), img.getHeight());
        selected = true;
        initialSelectSt = null;
        initialSelectEd = null;
        drawSelectedArea(img.getWidth(), img.getHeight(), true);
    }

    public void saveAsImage(String path) {
        panel.saveAsImage(mainCanvas, path);
    }

    public void saveAsImageRemote() {
        panel.saveAsImageRemote(mainCanvas);
    }

    public void undo() {
        Undoer.Record previous = undoer.undo(mainCanvas, savedCanvas);
        if (previous == null)
            return;
        mainCanvas = previous.mainCanvas;
        savedCanvas = previous.savedCanvas;
        repaint();
    }

    public void redo() {
        Undoer.Record next = undoer.redo();
        if (next == null)
            return;
        mainCanvas = next.mainCanvas;
        savedCanvas = next.savedCanvas;
        repaint();
    }

    public void pickColor(int x, int y, boolean isMainColor) {
        String color = ColorFiller.convertRGBHex(mainCanvas.getRGB(x, y));
        if (isMainColor)
            panel.toolbar.onMainColorChange(color);
        else
            panel.toolbar.onSecondaryColorChange(color);
    }

    public void fillColor(int x, int y, String color) {
        new ColorFiller(x, y, mainCanvas, color);
        repaint();
    }

    public void setMode(int mode, String... args) {
        this.mode = mode;
        if (mode == Canvas.mode_shape) {
            shape = args[0];
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(mainCanvas, 0, 0, null);
    }

    // mouse events
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
        } else if (mode == Canvas.mode_erase) {
            penDraw(e.getX(), e.getY(), "#" + Integer.toHexString(backgroundColor.getRGB()).substring(2));
        } else if (mode == Canvas.mode_select) {
            if (select_submode == Canvas.submode_select)
                drawSelectedArea(e.getX(), e.getY(), false);
            else if (select_submode == Canvas.submode_move)
                moveSelectedArea(e.getX(), e.getY());
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
            undoer.record(mainCanvas, savedCanvas);
        } else if (mode == Canvas.mode_shape) {
            undoer.record(mainCanvas, savedCanvas);
            startX = e.getX();
            startY = e.getY();
            savedCanvas = MyUtils.deepCopy(mainCanvas);
        } else if (mode == Canvas.mode_erase) {
            undoer.record(mainCanvas, savedCanvas);
            penDraw(e.getX(), e.getY(), "#" + Integer.toHexString(backgroundColor.getRGB()).substring(2));
        } else if (mode == Canvas.mode_fill) {
            undoer.record(mainCanvas, savedCanvas);
            if (SwingUtilities.isRightMouseButton(e))
                fillColor(e.getX(), e.getY(), secondaryColor);
            else
                fillColor(e.getX(), e.getY(), mainColor);
        } else if (mode == Canvas.mode_select) {
            if (selected) {
                FormattedPoints formatted = new FormattedPoints(selectSt.x, selectSt.y, selectEd.x, selectEd.y);
                if (formatted.inside(e.getX(), e.getY())) {
                    select_submode = Canvas.submode_move;
                    moveSt = new Point(e.getX(), e.getY());
                    return;
                }
                undoer.record(savedCanvas, savedCanvas);
                applySelectedArea();
            }
            select_submode = Canvas.submode_select;
            selectSt = new Point(e.getX(), e.getY());
            initialSelectSt = new Point(e.getX(), e.getY());
            selected = false;
            if (savedCanvas == null)
                savedCanvas = MyUtils.deepCopy(mainCanvas);
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
        } else if (mode == Canvas.mode_select) {
            if (select_submode == Canvas.submode_select) {
                selectEd = new Point(e.getX(), e.getY());
                initialSelectEd = new Point(selectEd.x, selectEd.y);
                FormattedPoints formatted = new FormattedPoints(selectSt.x, selectSt.y, selectEd.x, selectEd.y);
                if (formatted.getWidth() == 0 || formatted.getHeight() == 0)
                    return;
                clip = savedCanvas.getSubimage(formatted.topLeft.x, formatted.topLeft.y, formatted.getWidth(),
                        formatted.getHeight());
                selected = true;
            } else if (select_submode == Canvas.submode_move) {
                Point offset = new Point(e.getX() - moveSt.x, e.getY() - moveSt.y);
                selectSt = new Point(selectSt.x + offset.x, selectSt.y + offset.y);
                selectEd = new Point(selectEd.x + offset.x, selectEd.y + offset.y);
            }
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
}
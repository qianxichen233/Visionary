import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.border.Border;
import java.awt.image.BufferedImage;

public class client {
    public static void main(String[] args) {
        new DrawingPanel();
    }
}

class DrawingPanel {
    private JFrame jf = new JFrame("Visionary");

    public DrawingPanel() {
        jf.setSize(1250, 700);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Canvas();
        Toolbar toolbar = new Toolbar(canvas);
        jf.add(canvas);
        jf.add(toolbar);

        jf.setLocationRelativeTo(null);
        jf.setLayout(null);
        jf.getContentPane().setBackground(Color.decode("#394e5e"));
        jf.setVisible(true);
        jf.setResizable(false);
    }
}

class Canvas extends JPanel implements MouseListener, MouseMotionListener {
    private static final int width = 800;
    private static final int height = 700;
    private static final int padding = 30;

    private BufferedImage bufferedImage;

    private volatile String mainColor = "#000000";
    private volatile String secondaryColor = "#FFFFFF";
    private volatile int size = 5;

    public Canvas() {
        super();
        addMouseListener(this);
        addMouseMotionListener(this);
        setBounds(padding, padding, width - 3 * padding, height - 3 * padding);
        setLayout(null);
        setBackground(Color.white);
        bufferedImage = new BufferedImage(
                getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB);
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
        g.fillOval(x, y, size, size);
        repaint();
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

class Toolbar extends JPanel {
    private static final int width = 450;
    private static final int height = 700;
    private static final int padding = 30;
    private static final String[] defaultColorList = {
            "#FFFFFF", // white
            "#C0C0C0", // silver
            "#808080", // gray
            "#000000", // black
            "#FF0000", // red
            "#800000", // maroon
            "#FFFF00", // yellow
            "#808000", // olive
            "#00FF00", // lime
            "#008000", // green
            "#00FFFF", // aqua
            "#008080", // teal
            "#0000FF", // blue
            "#000080", // navy
            "#FF00FF", // fuchsia
            "#800080", // purple
    };

    private Canvas canvas;
    private String mainColor = "#000000";
    private String secondaryColor = "#FFFFFF";
    private int size = 5;

    private ArrayList<String> ColorList = new ArrayList<String>();

    private ColorPanel colorPanel;

    private class ColorPanel extends JPanel {
        private static final int padding = 10;
        private static final int height = 100;
        private static final int columns = 8;
        private static final int boxSize = 20;
        private static final int displayBoxSize = 30;

        public ColorPanel() {
            super();
            int width = getActualWidth() - 2 * padding;
            setBounds(padding, padding, width, height);
            setBackground(Color.white);

            int gap = (width - boxSize * columns) / (columns + 1);
            int curX = gap, curY = 50;
            for (int i = 0; i < ColorList.size(); ++i) {
                if (i % columns == 0 && i != 0) {
                    curX = gap;
                    curY += boxSize + 10;
                }

                ColorBox box = new ColorBox(ColorList.get(i));
                box.setBounds(curX, curY, boxSize, boxSize);
                add(box);
                curX += gap + boxSize;
            }
            setLayout(null);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getActualWidth() - 2 * padding;
            int gap = (width - boxSize * columns) / (columns + 1);
            g.setColor(Color.decode(mainColor));
            g.fillRect(gap, 10, displayBoxSize, displayBoxSize);
            g.setColor(Color.decode(secondaryColor));
            g.fillRect(width - displayBoxSize - gap, 10, displayBoxSize, displayBoxSize);
            g.setColor(Color.gray); // border color
            g.drawRect(gap, 10, displayBoxSize, displayBoxSize);
            g.drawRect(width - displayBoxSize - gap, 10, displayBoxSize, displayBoxSize);
        }
    }

    private class ColorBox extends JPanel implements MouseListener {
        private String color;

        ColorBox(String color) {
            super();
            this.color = color;
            setBackground(Color.decode(color));
            addMouseListener(this);
            Border blackline = BorderFactory.createLineBorder(Color.gray);
            setBorder(blackline);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e))
                onSecondaryColorChange(color);
            else
                onMainColorChange(color);
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

    public Toolbar(Canvas canvas) {
        super();
        this.canvas = canvas;
        setBounds(padding + 800, padding, width - 3 * padding, height - 3 * padding);
        setLayout(null);
        setBackground(Color.white);

        for (String color : defaultColorList)
            ColorList.add(color);

        colorPanel = new ColorPanel();
        add(colorPanel);
        colorPanel.repaint();
    }

    private void onMainColorChange(String color) {
        this.mainColor = color;
        canvas.setMainColor(this.mainColor);
        colorPanel.repaint();
    }

    private void onSecondaryColorChange(String color) {
        this.secondaryColor = color;
        canvas.setSecondaryColor(this.secondaryColor);
        colorPanel.repaint();
    }

    private void onSizeChange(int size) {
        this.size = size;
        canvas.setSize(this.size);
    }

    private static int getActualWidth() {
        return width - 3 * padding;
    }

    private static int getActualHeight() {
        return height - 3 * padding;
    }
}
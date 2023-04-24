package client.Toolbar;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.*;

public class ColorPanel extends JPanel {
    private static final int padding = 10;
    public static final int height = 100;
    private static final int columns = 8;
    private static final int boxSize = 20;
    private static final int displayBoxSize = 30;
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

    private Toolbar toolbar;
    ArrayList<String> ColorList = new ArrayList<String>();

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
                toolbar.onSecondaryColorChange(color);
            else
                toolbar.onMainColorChange(color);
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

    public ColorPanel(Toolbar toolbar, int Hoffset) {
        super();
        for (String color : defaultColorList)
            ColorList.add(color);
        int width = Toolbar.getActualWidth() - 2 * padding;
        this.toolbar = toolbar;
        // setBounds(padding, padding + Hoffset, width, height);
        setPreferredSize(new Dimension(width, height));
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
        int width = Toolbar.getActualWidth() - 2 * padding;
        int gap = (width - boxSize * columns) / (columns + 1);

        g.setColor(Color.decode(toolbar.mainColor));
        g.fillRect(gap, 10, displayBoxSize, displayBoxSize);
        g.setColor(Color.decode(toolbar.secondaryColor));
        g.fillRect(width - displayBoxSize - gap, 10, displayBoxSize, displayBoxSize);
        g.setColor(Color.gray); // border color
        g.drawRect(gap, 10, displayBoxSize, displayBoxSize);
        g.drawRect(width - displayBoxSize - gap, 10, displayBoxSize, displayBoxSize);
    }

    static public int getCHeight() {
        return height + padding;
    }
}

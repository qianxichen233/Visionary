package client.Toolbar;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

import client.Canvas.Canvas;

public class ShapePanel extends JPanel {
    private static final int padding = 10;
    private static final int height = 30;

    private static final int options = 4;

    Toolbar toolbar;

    ShapePanel(Toolbar toolbar, int Hoffset) {
        super();
        this.toolbar = toolbar;
        int width = Toolbar.getActualWidth() - 2 * padding;

        JButton lineOption = new JButton("Line");
        JButton rectOption = new JButton("Rect");
        JButton cirOption = new JButton("Circle");
        JButton triOption = new JButton("Triangle");
        lineOption.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rectOption.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cirOption.setCursor(new Cursor(Cursor.HAND_CURSOR));
        triOption.setCursor(new Cursor(Cursor.HAND_CURSOR));

        int buttonPadding = (width - lineOption.getPreferredSize().width
                - rectOption.getPreferredSize().width
                - cirOption.getPreferredSize().width
                - triOption.getPreferredSize().width) / (options + 1);

        int curX = buttonPadding;

        lineOption.setBounds(curX, 5, lineOption.getPreferredSize().width, height);
        lineOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapePanel.this.toolbar.setMode(Canvas.mode_shape, "Line");
            }
        });
        curX += lineOption.getPreferredSize().width + buttonPadding;

        rectOption.setBounds(curX, 5, rectOption.getPreferredSize().width, height);
        rectOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapePanel.this.toolbar.setMode(Canvas.mode_shape, "Rectangle");
            }
        });
        curX += rectOption.getPreferredSize().width + buttonPadding;

        cirOption.setBounds(curX, 5, cirOption.getPreferredSize().width, height);
        cirOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapePanel.this.toolbar.setMode(Canvas.mode_shape, "Circle");
            }
        });
        curX += cirOption.getPreferredSize().width + buttonPadding;

        triOption.setBounds(curX, 5, triOption.getPreferredSize().width, height);
        triOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapePanel.this.toolbar.setMode(Canvas.mode_shape, "Triangle");
            }
        });

        add(lineOption);
        add(rectOption);
        add(cirOption);
        add(triOption);

        // setBounds(padding, padding + Hoffset, width, height);
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
    }

    public void setFocus(String shape) {
    }

    public void loseFocus() {
    }

    public static int getCHeight() {
        return height + padding;
    }
}

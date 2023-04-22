package client.Toolbar;

import java.awt.event.*;
import javax.swing.*;

import client.Canvas.Canvas;

public class ShapePanel extends JPanel {
    private static final int padding = 10;
    private static final int height = 30;

    private static final int buttonPadding = 10;

    Toolbar toolbar;

    ShapePanel(Toolbar toolbar, int Hoffset) {
        super();
        this.toolbar = toolbar;
        int width = Toolbar.getActualWidth() - 2 * padding;

        JButton lineOption = new JButton("Line");
        JButton rectOption = new JButton("Rect");
        JButton cirOption = new JButton("Circle");

        int curX = buttonPadding;

        lineOption.setBounds(curX, 0, lineOption.getPreferredSize().width, height);
        lineOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapePanel.this.toolbar.setMode(Canvas.mode_shape, "Line");
            }
        });
        curX += lineOption.getPreferredSize().width + buttonPadding;

        rectOption.setBounds(curX, 0, rectOption.getPreferredSize().width, height);
        rectOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapePanel.this.toolbar.setMode(Canvas.mode_shape, "Rectangle");
            }
        });
        curX += rectOption.getPreferredSize().width + buttonPadding;

        cirOption.setBounds(curX, 0, cirOption.getPreferredSize().width, height);
        cirOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShapePanel.this.toolbar.setMode(Canvas.mode_shape, "Circle");
            }
        });

        add(lineOption);
        add(rectOption);
        add(cirOption);

        setBounds(padding, padding + Hoffset, width, height);
        setLayout(null);
    }

    public static int getCHeight() {
        return height + padding;
    }
}

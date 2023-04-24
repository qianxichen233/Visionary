package client.Toolbar;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

import client.Canvas.Canvas;

public class PenModePanel extends JPanel {
    private static final int padding = 10;
    private static final int height = 30;

    private static final int column = 4;
    private static final int row = 2;

    Toolbar toolbar;

    PenModePanel(Toolbar toolbar, int Hoffset) {
        super();
        this.toolbar = toolbar;
        int width = Toolbar.getActualWidth() - 2 * padding;

        JButton penOption = new JButton("Pen");
        JButton pickerOption = new JButton("Picker");
        JButton eraserOption = new JButton("Eraser");
        JButton fillOption = new JButton("Fill");
        JButton selectOption = new JButton("Select");
        penOption.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pickerOption.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eraserOption.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fillOption.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectOption.setCursor(new Cursor(Cursor.HAND_CURSOR));

        int buttonPadding = (width - penOption.getPreferredSize().width
                - pickerOption.getPreferredSize().width
                - eraserOption.getPreferredSize().width
                - fillOption.getPreferredSize().width) / (column + 1);

        int curX = buttonPadding;

        penOption.setBounds(curX, 5, penOption.getPreferredSize().width, height);
        penOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenModePanel.this.toolbar.setMode(Canvas.mode_pen);
            }
        });
        curX += penOption.getPreferredSize().width + buttonPadding;

        pickerOption.setBounds(curX, 5, pickerOption.getPreferredSize().width, height);
        pickerOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenModePanel.this.toolbar.setMode(Canvas.mode_pick);
            }
        });
        curX += pickerOption.getPreferredSize().width + buttonPadding;

        eraserOption.setBounds(curX, 5, eraserOption.getPreferredSize().width, height);
        eraserOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenModePanel.this.toolbar.setMode(Canvas.mode_erase);
            }
        });
        curX += eraserOption.getPreferredSize().width + buttonPadding;

        fillOption.setBounds(curX, 5, fillOption.getPreferredSize().width, height);
        fillOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenModePanel.this.toolbar.setMode(Canvas.mode_fill);
            }
        });
        curX = buttonPadding;

        selectOption.setBounds(curX, height + 5, selectOption.getPreferredSize().width, height);
        selectOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenModePanel.this.toolbar.setMode(Canvas.mode_select);
            }
        });

        add(penOption);
        add(pickerOption);
        add(eraserOption);
        add(fillOption);
        add(selectOption);

        // setBounds(padding, padding + Hoffset, width, height);
        setPreferredSize(new Dimension(width, height * row));
        setLayout(null);
    }

    public void setFocus(int mode) {
    }

    public void loseFocus() {
    }

    public static int getCHeight() {
        return height * row + padding;
    }
}

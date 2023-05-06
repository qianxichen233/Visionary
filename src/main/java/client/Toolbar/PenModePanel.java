package client.Toolbar;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

import client.Canvas.Canvas;
import client.Toolbar.Buttons.MyButton;

public class PenModePanel extends JPanel {
    private static final int padding = 10;
    private static final int height = 25;

    private static final int column = 4;
    private static final int row = 2;

    Toolbar toolbar;

    private MyButton penOption;
    private MyButton pickerOption;
    private MyButton eraserOption;
    private MyButton fillOption;
    private MyButton selectOption;

    PenModePanel(Toolbar toolbar, int Hoffset) {
        super();
        this.toolbar = toolbar;
        int width = Toolbar.getActualWidth() - 2 * padding;

        penOption = new MyButton("Pen");
        pickerOption = new MyButton("Picker");
        eraserOption = new MyButton("Eraser");
        fillOption = new MyButton("Fill");
        selectOption = new MyButton("Select");
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

        selectOption.setBounds(curX, height + 10, selectOption.getPreferredSize().width, height);
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
        setPreferredSize(new Dimension(width, height * row + (row - 1) * 5));
        setLayout(null);
    }

    public void setFocus(int mode) {
        penOption.setFocus(mode == Canvas.mode_pen);
        pickerOption.setFocus(mode == Canvas.mode_pick);
        eraserOption.setFocus(mode == Canvas.mode_erase);
        fillOption.setFocus(mode == Canvas.mode_fill);
        selectOption.setFocus(mode == Canvas.mode_select);
    }

    public void loseFocus() {
        penOption.unFocus();
        pickerOption.unFocus();
        eraserOption.unFocus();
        fillOption.unFocus();
        selectOption.unFocus();
    }

    public static int getCHeight() {
        return height * row + padding;
    }
}

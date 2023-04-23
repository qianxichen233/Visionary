package client.Toolbar;

import java.awt.event.*;
import javax.swing.*;

import client.Canvas.Canvas;

public class PenModePanel extends JPanel {
    private static final int padding = 10;
    private static final int height = 30;

    private static final int options = 4;

    Toolbar toolbar;

    PenModePanel(Toolbar toolbar, int Hoffset) {
        super();
        this.toolbar = toolbar;
        int width = Toolbar.getActualWidth() - 2 * padding;

        JButton penOption = new JButton("Pen");
        JButton pickerOption = new JButton("Picker");
        JButton eraserOption = new JButton("Eraser");
        JButton fillOption = new JButton("Fill");

        int buttonPadding = (width - penOption.getPreferredSize().width
                - pickerOption.getPreferredSize().width
                - eraserOption.getPreferredSize().width
                - fillOption.getPreferredSize().width) / (options + 1);

        int curX = buttonPadding;

        penOption.setBounds(curX, 0, penOption.getPreferredSize().width, height);
        penOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenModePanel.this.toolbar.setMode(Canvas.mode_pen);
            }
        });
        curX += penOption.getPreferredSize().width + buttonPadding;

        pickerOption.setBounds(curX, 0, pickerOption.getPreferredSize().width, height);
        pickerOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenModePanel.this.toolbar.setMode(Canvas.mode_pick);
            }
        });
        curX += pickerOption.getPreferredSize().width + buttonPadding;

        eraserOption.setBounds(curX, 0, eraserOption.getPreferredSize().width, height);
        eraserOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenModePanel.this.toolbar.setMode(Canvas.mode_erase);
            }
        });
        curX += eraserOption.getPreferredSize().width + buttonPadding;

        fillOption.setBounds(curX, 0, fillOption.getPreferredSize().width, height);
        fillOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenModePanel.this.toolbar.setMode(Canvas.mode_fill);
            }
        });
        curX += fillOption.getPreferredSize().width + buttonPadding;

        add(penOption);
        add(pickerOption);
        add(eraserOption);
        add(fillOption);

        setBounds(padding, padding + Hoffset, width, height);
        setLayout(null);
    }

    public void setFocus(int mode) {
    }

    public void loseFocus() {
    }

    public static int getCHeight() {
        return height + padding;
    }
}

package client.Toolbar;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.text.*;
import java.awt.*;

public class SizePanel extends JPanel {
    private static final int padding = 10;
    public static final int height = 30;
    public static final int _min = 2;
    public static final int _max = 99;

    JFormattedTextField sizeField;
    Toolbar toolbar;

    SizePanel(Toolbar toolbar, int Hoffset) {
        super();
        this.toolbar = toolbar;
        int width = Toolbar.getActualWidth() - 2 * padding;
        JLabel text = new JLabel("Size: ");
        text.setBounds(30, 5, 100, height);
        JButton minus = new JButton("-");
        minus.setBounds(100, 5, 50, height);
        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSizeChange(-1);
            }
        });
        JButton add = new JButton("+");
        add.setBounds(200, 5, 50, height);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSizeChange(1);
            }
        });

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(_min);
        formatter.setMaximum(_max);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        sizeField = new JFormattedTextField(formatter);
        sizeField.setText(toolbar.size + "");
        sizeField.setBounds(150, 5, 50, height);
        sizeField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int current = Integer.parseInt(sizeField.getText());
                SizePanel.this.toolbar.onSizeChange(current);
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }
        });

        this.add(text);
        this.add(minus);
        this.add(sizeField);
        this.add(add);
        // setBounds(padding, padding + Hoffset, width, height);
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
    }

    private void onSizeChange(int diff) {
        int current = Integer.parseInt(sizeField.getText());
        current += diff;
        if (current > _max)
            current = _max;
        if (current < _min)
            current = _min;

        toolbar.onSizeChange(current);
        sizeField.setText(current + "");
    }

    public static int getCHeight() {
        return height + padding;
    }
}

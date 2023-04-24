package client.Toolbar.Buttons;

import javax.swing.*;

import client.Toolbar.*;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.*;

public class ReturnButton extends JPanel {
    public static final int height = 30;
    private static final int padding = 10;
    Toolbar toolbar;

    public ReturnButton(Toolbar toolbar, String title, int Hoffset) {
        super();
        this.toolbar = toolbar;

        JButton button = new JButton(title);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // setBounds(0, Hoffset, Toolbar.getActualWidth(), height);
        setPreferredSize(new Dimension(Toolbar.getActualWidth() - 2 * padding, height));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReturnButton.this.toolbar.onReturn();
            }
        });
        add(button);
    }
}

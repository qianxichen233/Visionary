package client.Toolbar.Buttons;

import javax.swing.JButton;

import client.Toolbar.*;

import java.awt.event.*;

public class ReturnButton extends JButton {
    public static final int height = 30;
    Toolbar toolbar;

    public ReturnButton(Toolbar toolbar, String title, int Hoffset) {
        super();
        this.toolbar = toolbar;
        setText(title);
        setBounds(0, Hoffset, Toolbar.getActualWidth(), height);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReturnButton.this.toolbar.onReturn();
            }
        });
    }
}

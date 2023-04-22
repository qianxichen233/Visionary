package client.Toolbar.Buttons;

import javax.swing.JButton;

import client.Toolbar.*;

import java.awt.event.*;

public class ReturnButton extends JButton {
    Toolbar toolbar;

    public ReturnButton(Toolbar toolbar, String title) {
        super();
        this.toolbar = toolbar;
        setText(title);
        setBounds(0, ColorPanel.getCHeight() + SizePanel.getCHeight() + 90, Toolbar.getActualWidth(), 30);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReturnButton.this.toolbar.onReturn();
            }
        });
    }
}

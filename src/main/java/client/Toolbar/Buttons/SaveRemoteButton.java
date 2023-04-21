package client.Toolbar.Buttons;

import javax.swing.JButton;

import client.Toolbar.*;

import java.awt.event.*;
import java.awt.*;

public class SaveRemoteButton extends JButton {
    Toolbar toolbar;

    public SaveRemoteButton(Toolbar toolbar, String title) {
        super();
        this.toolbar = toolbar;
        setText(title);
        setBounds(0, ColorPanel.getCHeight() + SizePanel.getCHeight() + 60, Toolbar.getActualWidth(), 30);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveRemoteButton.this.toolbar.onSaveRemote();
            }
        });
    }
}

package client.Toolbar.Buttons;

import javax.swing.JButton;

import client.Toolbar.*;

import java.awt.event.*;
import java.awt.*;

public class SaveRemoteButton extends JButton {
    public static final int height = 30;
    Toolbar toolbar;

    public SaveRemoteButton(Toolbar toolbar, String title, int Hoffset) {
        super();
        this.toolbar = toolbar;
        setText(title);
        setBounds(0, Hoffset, Toolbar.getActualWidth(), height);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaveRemoteButton.this.toolbar.onSaveRemote();
            }
        });
    }
}

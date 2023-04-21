package com.qx.client.Toolbar.Buttons;

import com.qx.client.Toolbar.*;

import javax.swing.JButton;
import java.awt.event.*;
import java.awt.*;

public class SaveButton extends JButton {
    Toolbar toolbar;

    public SaveButton(Toolbar toolbar, String title) {
        super();
        this.toolbar = toolbar;
        setText(title);
        setBounds(0, ColorPanel.getCHeight() + SizePanel.getCHeight(), Toolbar.getActualWidth(), 30);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(new Frame(), "Save as png file", FileDialog.LOAD);
                fd.setMode(FileDialog.SAVE);
                fd.setDirectory(System.getProperty("user.dir") + "/myImages");
                fd.setFile("Untitled.png");
                fd.setVisible(true);
                if (fd.getFile() == null)
                    return;
                String path = fd.getDirectory() + fd.getFile();
                SaveButton.this.toolbar.onSaveLocal(path);
            }
        });
    }
}

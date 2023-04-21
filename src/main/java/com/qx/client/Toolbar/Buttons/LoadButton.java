package com.qx.client.Toolbar.Buttons;

import com.qx.client.Toolbar.*;

import javax.swing.JButton;
import java.awt.event.*;
import java.awt.*;

import java.io.*;

public class LoadButton extends JButton {
    Toolbar toolbar;

    public LoadButton(Toolbar toolbar, String title) {
        super();
        this.toolbar = toolbar;
        setText(title);
        setBounds(0, ColorPanel.getCHeight() + SizePanel.getCHeight() + 30, Toolbar.getActualWidth(), 30);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(new Frame(), "Choose a file", FileDialog.LOAD);
                fd.setDirectory(System.getProperty("user.dir") + "/myImages");
                fd.setFilenameFilter(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".png");
                    }
                });
                fd.setVisible(true);
                if (fd.getFile() == null)
                    return;
                String path = fd.getDirectory() + fd.getFile();
                LoadButton.this.toolbar.onLoad(path);
            }
        });
    }
}

package client.Toolbar.Buttons;

import javax.swing.JButton;

import client.Toolbar.*;

import java.awt.event.*;
import java.awt.*;

public class SaveButton extends JButton {
    public static final int height = 30;
    Toolbar toolbar;

    public SaveButton(Toolbar toolbar, String title, int Hoffset) {
        super();
        this.toolbar = toolbar;
        setText(title);
        setBounds(0, Hoffset, Toolbar.getActualWidth(), height);
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

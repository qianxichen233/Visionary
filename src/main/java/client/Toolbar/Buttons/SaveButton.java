package client.Toolbar.Buttons;

import javax.swing.*;

import client.Toolbar.*;

import java.awt.event.*;
import java.awt.*;

public class SaveButton extends JPanel {
    public static final int height = 30;
    private static final int padding = 10;
    Toolbar toolbar;

    public SaveButton(Toolbar toolbar, String title, int Hoffset) {
        super();
        this.toolbar = toolbar;

        JButton button = new JButton(title);
        button.setText(title);
        // setBounds(0, Hoffset, Toolbar.getActualWidth(), height);
        setPreferredSize(new Dimension(Toolbar.getActualWidth() - 2 * padding, height));
        button.addActionListener(new ActionListener() {
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
        add(button);
    }
}

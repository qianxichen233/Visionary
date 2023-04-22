package client.Toolbar;

import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;

public class FilenamePanel extends JPanel {
    private static final int padding = 10;
    public static final int height = 30;
    private static final int textPadding = 3;

    JTextArea filenameField;
    Toolbar toolbar;

    FilenamePanel(Toolbar toolbar, int Hoffset) {
        super();
        this.toolbar = toolbar;
        int width = Toolbar.getActualWidth() - 2 * padding;
        JLabel text = new JLabel("Filename: ");
        text.setBounds(30, 0, 100, height);

        filenameField = new JTextArea();
        filenameField.setText(toolbar.getFilename());
        filenameField.setMargin(new Insets(textPadding, textPadding, textPadding, textPadding));
        int filenameFieldHeight = filenameField.getPreferredSize().height;
        filenameField.setBounds(100, (height - filenameFieldHeight) / 2, 200, filenameFieldHeight);

        filenameField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String filename = filenameField.getText();
                FilenamePanel.this.toolbar.setFilename(filename);
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }
        });

        this.add(text);
        this.add(filenameField);
        setBounds(padding, padding + Hoffset, width, height);
        setLayout(null);
    }

    public static int getCHeight() {
        return height + padding;
    }
}
package client.Toolbar;

import java.awt.Color;

import javax.swing.*;

public class TextPanel extends JPanel {
    private static final int padding = 10;
    private static final int height = 30;

    Toolbar toolbar;

    TextPanel(Toolbar toolbar, String text, int Hoffset) {
        super();
        this.toolbar = toolbar;
        int width = Toolbar.getActualWidth() - 2 * padding;

        JLabel textLabel = new JLabel(text, SwingConstants.CENTER);
        textLabel.setForeground(Color.white);

        add(textLabel);

        setBounds(padding, padding + Hoffset, width, height);
        setBackground(Color.decode("#394e5e"));
    }

    public static int getCHeight() {
        return height + padding;
    }
}

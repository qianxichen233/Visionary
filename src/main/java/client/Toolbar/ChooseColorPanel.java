package client.Toolbar;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

public class ChooseColorPanel extends JPanel {
    private static final int padding = 10;
    private static final int height = 30;

    private static final int options = 2;

    Toolbar toolbar;

    ChooseColorPanel(Toolbar toolbar, int Hoffset) {
        super();
        this.toolbar = toolbar;
        int width = Toolbar.getActualWidth() - 2 * padding;

        JButton mainColorChooser = new JButton("Main Color");
        JButton secondaryColorChooser = new JButton("Secondary Color");

        int buttonPadding = (width - mainColorChooser.getPreferredSize().width
                - secondaryColorChooser.getPreferredSize().width) / (options + 1);

        int curX = buttonPadding;

        int gap = 5;

        mainColorChooser.setBounds(curX, gap, mainColorChooser.getPreferredSize().width, height);
        mainColorChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(new JFrame(), "Choose a color",
                        Color.decode(ChooseColorPanel.this.toolbar.mainColor));
                if (color == null)
                    return;
                ChooseColorPanel.this.toolbar.onMainColorChange(color);
            }
        });
        curX += mainColorChooser.getPreferredSize().width + buttonPadding;

        secondaryColorChooser.setBounds(curX, gap, secondaryColorChooser.getPreferredSize().width, height);
        secondaryColorChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(new JFrame(), "Choose a color",
                        Color.decode(ChooseColorPanel.this.toolbar.secondaryColor));
                if (color == null)
                    return;
                ChooseColorPanel.this.toolbar.onSecondaryColorChange(color);
            }
        });

        add(mainColorChooser);
        add(secondaryColorChooser);

        // setBounds(padding, padding + Hoffset, width, height);
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
    }

    public static int getCHeight() {
        return height + padding;
    }
}

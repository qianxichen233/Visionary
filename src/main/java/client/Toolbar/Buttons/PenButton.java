package client.Toolbar.Buttons;

import javax.swing.JButton;

import client.Canvas.Canvas;
import client.Toolbar.*;

import java.awt.event.*;

public class PenButton extends JButton {
    public static final int height = 30;
    Toolbar toolbar;

    public PenButton(Toolbar toolbar, String title, int Hoffset) {
        super();
        this.toolbar = toolbar;
        setText(title);
        setBounds(0, Hoffset, Toolbar.getActualWidth(), height);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PenButton.this.toolbar.setMode(Canvas.mode_pen);
            }
        });
    }
}

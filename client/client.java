import java.awt.*;
import javax.swing.*;

import Toolbar.Toolbar;
import Canvas.Canvas;

public class client {
    public static void main(String[] args) {
        new DrawingPanel();
    }
}

class DrawingPanel {
    private JFrame jf = new JFrame("Visionary");

    public DrawingPanel() {
        jf.setSize(1250, 700);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Canvas canvas = new Canvas();
        Toolbar toolbar = new Toolbar(canvas);
        jf.add(canvas);
        jf.add(toolbar);

        jf.setLocationRelativeTo(null);
        jf.setLayout(null);
        jf.getContentPane().setBackground(Color.decode("#394e5e"));
        jf.setVisible(true);
        jf.setResizable(false);
    }
}

package client.Panel;

import java.awt.*;
import javax.swing.*;

import client.ClientInstance;
import client.Canvas.Canvas;
import client.Toolbar.Toolbar;

public class DrawingPanel {
    private final ClientInstance client;

    private JFrame jf;

    public DrawingPanel(ClientInstance client) {
        System.out.println("drawing!");
        this.client = client;
        jf = client.jf;
        jf.getContentPane().removeAll();
        jf.getContentPane().invalidate();
        jf.getContentPane().validate();
        jf.getContentPane().repaint();

        Canvas canvas = new Canvas();
        Toolbar toolbar = new Toolbar(canvas);
        jf.add(canvas);
        jf.add(toolbar);

        jf.setLayout(null);
        jf.getContentPane().setBackground(Color.decode("#394e5e"));
        jf.getContentPane().repaint();
        jf.setVisible(true);
    }
}

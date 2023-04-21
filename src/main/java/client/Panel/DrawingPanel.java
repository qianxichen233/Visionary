package client.Panel;

import java.awt.*;
import javax.swing.*;

import client.ClientInstance;
import client.Canvas.Canvas;
import client.Toolbar.Toolbar;

public class DrawingPanel {
    private final ClientInstance client;

    private JFrame jf = new JFrame("Visionary");

    public DrawingPanel(ClientInstance client) {
        this.client = client;

        jf.setSize(ClientInstance.windowWidth, ClientInstance.windowHeight);
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

package client.Panel;

import java.awt.*;
import java.net.Socket;

import javax.swing.*;

import client.ClientInstance;
import client.Canvas.Canvas;
import client.Toolbar.Toolbar;

public class DrawingPanel {
    private final ClientInstance client;

    private Socket sock;
    private String username;

    private JFrame jf;

    public DrawingPanel(ClientInstance client, Socket sock, String username) {
        this.client = client;
        this.sock = sock;
        this.username = username;

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

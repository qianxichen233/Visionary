package client.Panel;

import java.awt.Color;

import javax.swing.*;

import client.ClientInstance;

public class MyPanel {
    protected final ClientInstance client;

    protected JFrame jf;

    public MyPanel(ClientInstance client) {
        this.client = client;
        this.jf = client.jf;
        resetPanel();
    }

    protected void resetPanel() {
        jf.getContentPane().removeAll();
        jf.getContentPane().invalidate();
        jf.getContentPane().setBackground(Color.decode("#394e5e"));
        jf.getContentPane().validate();
        jf.getContentPane().repaint();
    }
}

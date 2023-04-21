package client.Panel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.Socket;
import java.io.*;

import javax.swing.*;
import javax.imageio.ImageIO;

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

        Canvas canvas = new Canvas(this);
        Toolbar toolbar = new Toolbar(canvas);
        jf.add(canvas);
        jf.add(toolbar);

        jf.setLayout(null);
        jf.getContentPane().setBackground(Color.decode("#394e5e"));
        jf.getContentPane().repaint();
        jf.setVisible(true);
    }

    public void saveAsImage(BufferedImage image, String path) {
        if (!path.endsWith(".png")) {
            path = path + ".png";
        }
        File outputfile = new File(path);
        try {
            if (!ImageIO.write(image, "png", outputfile)) {
                System.out.println("failed");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void saveAsImageRemote(BufferedImage image) {
        try {
            PrintStream sout = new PrintStream(sock.getOutputStream());
            sout.println("save");

            OutputStream out = sock.getOutputStream();

            ByteArrayOutputStream bScrn = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bScrn);
            byte imgBytes[] = bScrn.toByteArray();
            bScrn.close();

            out.write((Integer.toString(imgBytes.length)).getBytes());
            out.write(imgBytes, 0, imgBytes.length);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

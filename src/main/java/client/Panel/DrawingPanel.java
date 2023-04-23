package client.Panel;

import java.awt.image.BufferedImage;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;

import javax.imageio.ImageIO;

import client.ClientInstance;
import client.Canvas.Canvas;
import client.Toolbar.Toolbar;

public class DrawingPanel extends MyPanel {
    private Socket sock;
    public String filename;
    private int ID;

    private boolean newDrawing;

    public Canvas canvas;
    public Toolbar toolbar;

    public DrawingPanel(ClientInstance client, Socket sock, String filename, BufferedImage bufferedImage, int ID) {
        super(client);
        this.sock = sock;
        this.filename = filename;
        this.ID = ID;
        newDrawing = false;
        initWithImage(bufferedImage);
    }

    public DrawingPanel(ClientInstance client, Socket sock, String filename, BufferedImage bufferedImage) {
        super(client);
        this.sock = sock;
        this.filename = filename;
        newDrawing = true;
        initWithImage(bufferedImage);
    }

    public DrawingPanel(ClientInstance client, Socket sock, String filename) {
        super(client);
        this.sock = sock;
        this.filename = filename;
        newDrawing = true;
        initWithoutImage();
    }

    private void initWithImage(BufferedImage image) {
        canvas = new Canvas(this, image);
        toolbar = new Toolbar(this);
        jf.add(canvas);
        jf.add(toolbar);

        jf.setLayout(null);
        jf.getContentPane().repaint();
        jf.setVisible(true);
    }

    private void initWithoutImage() {
        canvas = new Canvas(this);
        toolbar = new Toolbar(this);
        jf.add(canvas);
        jf.add(toolbar);

        jf.setLayout(null);
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
        if (newDrawing) {
            try {
                Scanner sin = new Scanner(sock.getInputStream());
                PrintStream sout = new PrintStream(sock.getOutputStream());
                sout.println("save");
                sout.println(filename);

                OutputStream out = sock.getOutputStream();

                ByteArrayOutputStream bScrn = new ByteArrayOutputStream();
                ImageIO.write(image, "png", bScrn);
                byte imgBytes[] = bScrn.toByteArray();
                bScrn.close();

                out.write((Integer.toString(imgBytes.length)).getBytes());
                out.write(imgBytes, 0, imgBytes.length);
                ID = Integer.parseInt(sin.nextLine());
                newDrawing = false;
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            try {
                PrintStream sout = new PrintStream(sock.getOutputStream());
                sout.println("update");
                sout.println(ID);
                sout.println(filename);

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

    public void onReturn() {
        client.drawingListPage();
    }
}

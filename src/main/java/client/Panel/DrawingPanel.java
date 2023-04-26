package client.Panel;

import java.awt.image.BufferedImage;
import java.net.Socket;
import java.io.*;
import javax.swing.*;

import javax.imageio.ImageIO;

import client.ClientInstance;
import client.SessionManager;
import client.Canvas.Canvas;
import client.Toolbar.Toolbar;
import client.utils.ImageEncryptor;
import client.utils.MyUtils;

public class DrawingPanel extends MyPanel {
    private SessionManager sessionManager;
    private ImageEncryptor imageEncryptor;
    public String filename;
    private int ID;

    private boolean newDrawing;

    public Canvas canvas;
    public Toolbar toolbar;

    public DrawingPanel(ClientInstance client, SessionManager sessionManager, ImageEncryptor imageEncryptor,
            String filename,
            BufferedImage bufferedImage, int ID) {
        super(client);
        this.sessionManager = sessionManager;
        this.imageEncryptor = imageEncryptor;
        this.filename = filename;
        this.ID = ID;
        newDrawing = false;
        initWithImage(bufferedImage);
    }

    public DrawingPanel(ClientInstance client, SessionManager sessionManager, ImageEncryptor imageEncryptor,
            String filename,
            BufferedImage bufferedImage) {
        super(client);
        this.sessionManager = sessionManager;
        this.imageEncryptor = imageEncryptor;
        this.filename = filename;
        newDrawing = true;
        initWithImage(bufferedImage);
    }

    public DrawingPanel(ClientInstance client, SessionManager sessionManager, ImageEncryptor imageEncryptor,
            String filename) {
        super(client);
        this.sessionManager = sessionManager;
        this.imageEncryptor = imageEncryptor;
        this.filename = filename;
        newDrawing = true;
        initWithoutImage();
    }

    private void initWithImage(BufferedImage image) {
        canvas = new Canvas(this, image);
        toolbar = new Toolbar(this);
        JScrollPane scroller = new JScrollPane(toolbar);
        scroller.setBounds(Toolbar.padding + 800, Toolbar.padding, Toolbar.width - 3 * Toolbar.padding,
                Toolbar.height - 3 * Toolbar.padding);
        jf.add(canvas);
        jf.add(scroller);

        jf.setLayout(null);
        jf.getContentPane().repaint();
        jf.setVisible(true);
    }

    private void initWithoutImage() {
        canvas = new Canvas(this);
        toolbar = new Toolbar(this);
        JScrollPane scroller = new JScrollPane(toolbar);
        scroller.setBounds(Toolbar.padding + 800, Toolbar.padding, Toolbar.width - 3 * Toolbar.padding,
                Toolbar.height - 3 * Toolbar.padding);
        jf.add(canvas);
        jf.add(scroller);

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
                SessionManager.MySock mySock = sessionManager.newSock("save", true);
                if (mySock == null)
                    return;
                mySock.sout.println(filename);

                ImageEncryptor.EncryptedImage main = imageEncryptor.encrypt(image);
                ImageEncryptor.EncryptedImage thumb = imageEncryptor.encrypt(MyUtils.resize(image, 0.2));
                MyUtils.sendByteArray(mySock.sock, main.iv);
                MyUtils.sendByteArray(mySock.sock, thumb.iv);

                sendImage(main.image, mySock.sock);
                sendImage(thumb.image, mySock.sock);
                ID = Integer.parseInt(mySock.sin.nextLine());
                mySock.close();
                newDrawing = false;
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            try {
                SessionManager.MySock mySock = sessionManager.newSock("update", true);
                if (mySock == null)
                    return;
                mySock.sout.println(ID);
                mySock.sout.println(filename);

                ImageEncryptor.EncryptedImage main = imageEncryptor.encrypt(image);
                ImageEncryptor.EncryptedImage thumb = imageEncryptor.encrypt(MyUtils.resize(image, 0.2));
                MyUtils.sendByteArray(mySock.sock, main.iv);
                MyUtils.sendByteArray(mySock.sock, thumb.iv);

                sendImage(main.image, mySock.sock);
                sendImage(thumb.image, mySock.sock);
                mySock.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void onReturn() {
        client.drawingListPage();
    }

    private static void sendImage(BufferedImage image, Socket sock) throws IOException {
        OutputStream out = sock.getOutputStream();
        InputStream in = sock.getInputStream();

        ByteArrayOutputStream bScrn = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bScrn);
        byte imgBytes[] = bScrn.toByteArray();
        bScrn.close();

        out.write((Integer.toString(imgBytes.length)).getBytes());
        in.read();
        out.write(imgBytes, 0, imgBytes.length);
    }
}

package client;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import client.Panel.*;
import client.utils.ImageEncryptor;

public class ClientInstance extends Thread {
    public static final int windowHeight = 700;
    public static final int windowWidth = 1250;

    public JFrame jf = new JFrame("Visionary");

    public SessionManager sessionManager;
    public ImageEncryptor imageEncryptor;

    private String username;

    public ClientInstance(String IP, int port) {
        sessionManager = new SessionManager(IP, port);

        jf.setSize(ClientInstance.windowWidth, ClientInstance.windowHeight);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
    }

    @Override
    public void run() {
        loginPage();
    }

    public void login(String username) {
        this.username = username;
        drawingListPage();
    }

    public void loginPage() {
        new LoginPanel(this);
    }

    public void registerPage() {
        new RegisterPanel(this);
    }

    public void drawingPage() {
        new DrawingPanel(this, sessionManager, imageEncryptor, "untitled");
    }

    public void drawingPage(BufferedImage image, String filename, int ID) {
        new DrawingPanel(this, sessionManager, imageEncryptor, filename, image, ID);
    }

    public void drawingPage(BufferedImage image, String filename) {
        new DrawingPanel(this, sessionManager, imageEncryptor, filename, image);
    }

    public void drawingListPage() {
        new DrawingListPanel(this, sessionManager, imageEncryptor, username);
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}

package client;

import java.net.Socket;

import javax.swing.JFrame;

import client.Panel.*;

public class ClientInstance extends Thread {
    public static final int windowHeight = 700;
    public static final int windowWidth = 1250;

    public JFrame jf = new JFrame("Visionary");

    public String serverIP;
    public int serverPort;

    private String username;
    private Socket sock;

    public ClientInstance(String IP, int port) {
        serverIP = IP;
        serverPort = port;

        jf.setSize(ClientInstance.windowWidth, ClientInstance.windowHeight);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
    }

    @Override
    public void run() {
        loginPage();
    }

    public void login(Socket sock, String username) {
        this.sock = sock;
        this.username = username;
        drawingPage();
    }

    public void loginPage() {
        new LoginPanel(this);
    }

    public void registerPage() {
        new RegisterPanel(this);
    }

    public void drawingPage() {
        new DrawingPanel(this, sock, username);
    }

    public Socket getSocket() {
        return sock;
    }
}

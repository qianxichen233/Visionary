package client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import client.Modal.Drawing;
import client.Panel.*;

public class ClientInstance extends Thread {
    public static final int windowHeight = 700;
    public static final int windowWidth = 1250;

    public JFrame jf = new JFrame("Visionary");

    public String serverIP;
    public int serverPort;

    private String username;
    private Socket sock;
    private ArrayList<Drawing> drawings = new ArrayList<Drawing>();

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
        try {
            Scanner sin = new Scanner(sock.getInputStream());
            int number = Integer.parseInt(sin.nextLine());
            for (int i = 0; i < number; ++i) {
                String filename = sin.nextLine();
                String createdAt = sin.nextLine();
                drawings.add(new Drawing(filename, createdAt));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        for (Drawing drawing : drawings) {
            System.out.println(drawing.filename);
            System.out.println(drawing.createdAt);
        }
        drawingPage();
    }

    public void loginPage() {
        new LoginPanel(this);
    }

    public void registerPage() {
        new RegisterPanel(this);
    }

    public void drawingPage() {
        new DrawingPanel(this, sock, "untitled");
    }

    public Socket getSocket() {
        return sock;
    }
}

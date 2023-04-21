package client.Panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;
import java.util.*;

import javax.swing.*;

import client.ClientInstance;
import client.Panel.utils.*;

public class LoginPanel {
    private final ClientInstance client;

    private JFrame jf;
    private String error;

    public LoginPanel(ClientInstance client) {
        this.client = client;

        jf = client.jf;
        jf.getContentPane().removeAll();
        jf.getContentPane().invalidate();
        jf.getContentPane().validate();
        jf.getContentPane().repaint();

        LoginForm loginForm = new LoginForm(this);
        loginForm.setBounds((ClientInstance.windowWidth - LoginForm.width) / 2,
                (ClientInstance.windowHeight - LoginForm.height) / 2,
                LoginForm.width,
                LoginForm.height);
        jf.add(loginForm);

        jf.setLayout(null);
        jf.getContentPane().setBackground(Color.decode("#394e5e"));
        jf.setVisible(true);
    }

    public void login(String username, String password) {
        try {
            Socket sock = new Socket(client.serverIP, client.serverPort);
            Scanner sin = new Scanner(sock.getInputStream());
            PrintStream sout = new PrintStream(sock.getOutputStream());

            sout.println("login");
            sout.println(username);
            sout.println(password);

            int result = Integer.parseInt(sin.nextLine());
            if (result != 200) {
                error = sin.nextLine();
                sock.close();
                return;
            }

            client.setSocket(sock);
        } catch (UnknownHostException e) {
            System.out.println("Server is closed!");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

class LoginForm extends JPanel {
    public static final int width = 500;
    public static final int height = 300;

    public LoginForm(final LoginPanel panel) {
        super();
        setBackground(Color.red);
        final InputField username = new InputField("Username");
        final InputField password = new InputField("Password");
        JButton button = new JButton("Log in");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.login(username.getText(), password.getText());
            }
        });
        add(username);
        add(password);
        add(button);
    }
}
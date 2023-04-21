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
import client.utils.*;

public class RegisterPanel {
    private final ClientInstance client;

    private JFrame jf;
    RegisterForm registerForm;

    public RegisterPanel(ClientInstance client) {
        this.client = client;
        jf = client.jf;
        jf.getContentPane().removeAll();
        jf.getContentPane().invalidate();
        jf.getContentPane().validate();
        jf.getContentPane().repaint();

        registerForm = new RegisterForm(this);
        registerForm.setBounds(
                (ClientInstance.windowWidth - RegisterForm.width) / 2,
                (ClientInstance.windowHeight - RegisterForm.height) / 2,
                RegisterForm.width,
                RegisterForm.height);
        jf.add(registerForm);

        jf.setLayout(null);
        jf.getContentPane().setBackground(Color.decode("#394e5e"));
        jf.setVisible(true);
    }

    public void register(String username, String password) {
        try {
            Socket sock = new Socket(client.serverIP, client.serverPort);
            Scanner sin = new Scanner(sock.getInputStream());
            PrintStream sout = new PrintStream(sock.getOutputStream());

            sout.println("register");
            sout.println(username);
            sout.println(password);

            int result = Integer.parseInt(sin.nextLine());
            System.out.println(result);
            if (result != 200) {
                registerForm.setError(sin.nextLine());
                sock.close();
                return;
            }
            sock.close();

            registerForm.setSuccess("Success!");
            new setTimeout(new setTimeoutEvent() {
                @Override
                public void performAction() {
                    client.loginPage();
                }
            }, 3000);
        } catch (UnknownHostException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

class RegisterForm extends JPanel {
    public static final int width = 500;
    public static final int height = 300;

    private String error = "";
    private String success = "";
    JLabel message = new JLabel();

    public RegisterForm(final RegisterPanel panel) {
        super();
        setLayout(new GridLayout(4, 1));
        final InputField username = new InputField("Username");
        final InputField password = new InputField("Password");
        JButton button = new JButton("Register");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.register(username.getText(), password.getText());
            }
        });
        add(username);
        add(password);
        add(button);
        add(message);
    }

    public void setError(String error) {
        this.error = error;
        renderMessage();
    }

    public void setSuccess(String success) {
        this.success = success;
        renderMessage();
    }

    public void clearError() {
        error = "";
        renderMessage();
    }

    public void clearSuccess() {
        success = "";
        renderMessage();
    }

    private void renderMessage() {
        if (!success.isEmpty()) {
            message.setForeground(Color.green);
            message.setText(success);
        } else if (!error.isEmpty()) {
            message.setForeground(Color.red);
            message.setText(error);
        } else
            message.setText("");
    }
}
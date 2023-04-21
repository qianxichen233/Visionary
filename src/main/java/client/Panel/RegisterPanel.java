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
import javax.swing.event.*;

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
        jf.getContentPane().repaint();
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

    public void swtich() {
        client.loginPage();
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
        setLayout(new GridLayout(6, 1));

        final InputField username = new InputField("Username");
        username.getJTextArea().getDocument().addDocumentListener(new DocumentListener() {
            public void update() {
                clearError();
                clearSuccess();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });

        final InputField password = new InputField("Password");
        password.getJTextArea().getDocument().addDocumentListener(new DocumentListener() {
            public void update() {
                clearError();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        JButton button = new JButton("Register");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!success.isEmpty())
                    return;
                String username_input = username.getText();
                String password_input = password.getText();
                if (username_input.isEmpty())
                    setError("Please Enter Username");
                else if (password_input.isEmpty())
                    setError("Please Enter Password");
                else
                    panel.register(username_input, password_input);
            }
        });
        buttonPanel.add(button);

        JPanel messagePanel = new JPanel(new GridBagLayout());
        messagePanel.add(message);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.add(new JLabel("Register"));

        JPanel switchPanel = new JPanel(new GridBagLayout());
        JButton switchButton = new JButton("Already Have Account? Login!");
        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.swtich();
            }
        });
        switchPanel.add(switchButton);

        add(titlePanel);
        add(username);
        add(password);
        add(messagePanel);
        add(buttonPanel);
        add(switchPanel);
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
            message.setForeground(Color.decode("#008000"));
            message.setText(success);
        } else if (!error.isEmpty()) {
            message.setForeground(Color.red);
            message.setText(error);
        } else
            message.setText("");
    }
}

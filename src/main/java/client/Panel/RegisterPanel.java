package client.Panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.*;

import client.ClientInstance;
import client.SessionManager;
import client.Panel.utils.*;
import client.utils.*;

public class RegisterPanel extends MyPanel {
    RegisterForm registerForm;

    public RegisterPanel(ClientInstance client) {
        super(client);

        registerForm = new RegisterForm(this);
        registerForm.setBounds(
                (ClientInstance.windowWidth - RegisterForm.width) / 2,
                (ClientInstance.windowHeight - RegisterForm.height) / 2,
                RegisterForm.width,
                RegisterForm.height);
        jf.add(registerForm);

        jf.setLayout(null);
        jf.getContentPane().repaint();
        jf.setVisible(true);
    }

    public void register(String username, String password) {
        SessionManager.MySock mySock = client.sessionManager.newSock("register", false);
        if (mySock == null)
            return;

        mySock.sout.println(username);
        mySock.sout.println(password);
        MyUtils.sendByteArray(mySock.sock, ImageEncryptor.genSalt());

        int result = Integer.parseInt(mySock.sin.nextLine());
        if (result != 200) {
            registerForm.setError(mySock.sin.nextLine());
            mySock.close();
            return;
        }
        mySock.close();

        registerForm.setSuccess("Success!");
        new setTimeout(new setTimeoutEvent() {
            @Override
            public void performAction() {
                client.loginPage();
            }
        }, 3000);
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

        final InputField password = new InputField("Password", InputField.TYPE_PASSWORD);
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
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        JLabel registerText = new JLabel("Register");
        registerText.setFont(new Font(registerText.getFont().getFontName(), Font.BOLD, 18));
        titlePanel.add(registerText);

        JPanel switchPanel = new JPanel(new GridBagLayout());
        JButton switchButton = new JButton("Already Have Account? Login!");
        switchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

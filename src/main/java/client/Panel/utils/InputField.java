package client.Panel.utils;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class InputField extends JPanel {
    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_PASSWORD = 1;

    private JTextComponent input;

    public InputField(String labelName) {
        this(labelName, TYPE_NORMAL);
    }

    public InputField(String labelName, int type) {
        setLayout(new GridBagLayout());
        JLabel label = new JLabel(labelName, SwingConstants.CENTER);
        JPanel gap = new JPanel();
        gap.setSize(20, 0);
        if (type == TYPE_PASSWORD) {
            input = new JPasswordField("", 20);
            input.setBorder(new JTextArea(1, 20).getBorder());
            // input.setBorder(null);
        } else if (type == TYPE_NORMAL) {
            input = new JTextArea(1, 20);
            input.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        }
        input.setMargin(new Insets(10, 5, 5, 5));
        add(label);
        add(gap);
        add(input);
    }

    public String getText() {
        return input.getText();
    }

    public JTextComponent getJTextArea() {
        return input;
    }
}
package client.Panel.utils;

import javax.swing.*;

public class InputField extends JPanel {
    private JTextArea input;

    public InputField(String labelName) {
        JLabel label = new JLabel(labelName);
        input = new JTextArea(1, 20);
        add(label);
        add(input);
    }

    public String getText() {
        return input.getText();
    }
}
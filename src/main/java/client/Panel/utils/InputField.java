package client.Panel.utils;

import java.awt.*;

import javax.swing.*;

public class InputField extends JPanel {
    private JTextArea input;

    public InputField(String labelName) {
        setLayout(new GridBagLayout());
        JLabel label = new JLabel(labelName, SwingConstants.CENTER);
        JPanel gap = new JPanel();
        gap.setSize(20, 0);
        input = new JTextArea(1, 20);
        input.setMargin(new Insets(10, 5, 5, 5));
        add(label);
        add(gap);
        add(input);
    }

    public String getText() {
        return input.getText();
    }

    public JTextArea getJTextArea() {
        return input;
    }
}
package client.Toolbar.Buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class MyButton extends JButton {
    private boolean focused = false;

    private final Color textColorUnfocused = Color.BLACK;
    private final Color bgColorUnfocused = Color.WHITE;

    private final Color textColorFocused = Color.BLACK;
    private final Color bgColorFocused = Color.LIGHT_GRAY;

    public MyButton(String text) {
        super(text);
        this.init();
    }

    public void focus() {
        if (focused)
            return;
        this.setForeground(textColorFocused);
        this.setBackground(bgColorFocused);
        focused = true;
        repaint();
    }

    public void unFocus() {
        if (!focused)
            return;
        this.setForeground(textColorUnfocused);
        this.setBackground(bgColorUnfocused);
        focused = false;
        repaint();
    }

    public void setFocus(boolean focus) {
        if (focus)
            focus();
        else
            unFocus();
    }

    private void init() {
        this.setForeground(textColorUnfocused);

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setFocusPainted(false);

        this.setBackground(bgColorUnfocused);
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);
        g2d.setColor(getBackground());
        g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
        g2d.setColor(Color.gray);
        g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
        g2d.setColor(getForeground());
        super.paintComponent(g2d);
        g2d.dispose();
    }
}

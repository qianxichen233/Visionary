package client.Toolbar;

import java.awt.*;
import javax.swing.*;

import client.Canvas.Canvas;
import client.Toolbar.Buttons.*;

public class Toolbar extends JPanel {
    private static final int width = 450;
    private static final int height = 700;
    private static final int padding = 30;

    private Canvas canvas;

    String mainColor = Canvas.defaultMainColor;
    String secondaryColor = Canvas.defaultSecondaryColor;
    int size = Canvas.defaultSize;

    private ColorPanel colorPanel;
    private SizePanel sizePanel;
    private SaveButton saveButton;
    private LoadButton loadButton;

    public Toolbar(Canvas canvas) {
        super();

        this.canvas = canvas;

        setBounds(padding + 800, padding, width - 3 * padding, height - 3 * padding);
        setLayout(null);
        setBackground(Color.white);

        colorPanel = new ColorPanel(this);
        sizePanel = new SizePanel(this);
        saveButton = new SaveButton(this, "Save");
        loadButton = new LoadButton(this, "Load");

        add(colorPanel);
        add(sizePanel);
        add(saveButton);
        add(loadButton);
    }

    void onMainColorChange(String color) {
        this.mainColor = color;
        canvas.setMainColor(this.mainColor);
        colorPanel.repaint();
    }

    void onSecondaryColorChange(String color) {
        this.secondaryColor = color;
        canvas.setSecondaryColor(this.secondaryColor);
        colorPanel.repaint();
    }

    void onSizeChange(int size) {
        this.size = size;
        canvas.setSize(this.size);
    }

    public void onSaveLocal(String path) {
        canvas.saveAsImage(path);
    }

    public void onLoad(String path) {
        canvas.loadImage(path);
    }

    public static int getActualWidth() {
        return width - 3 * padding;
    }
}

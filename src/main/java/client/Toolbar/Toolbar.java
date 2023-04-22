package client.Toolbar;

import java.awt.*;
import javax.swing.*;

import client.Canvas.Canvas;
import client.Panel.DrawingPanel;
import client.Toolbar.Buttons.*;

public class Toolbar extends JPanel {
    private static final int width = 450;
    private static final int height = 700;
    private static final int padding = 30;

    private Canvas canvas;
    private DrawingPanel panel;

    String mainColor = Canvas.defaultMainColor;
    String secondaryColor = Canvas.defaultSecondaryColor;
    int size = Canvas.defaultSize;

    private ColorPanel colorPanel;
    private SizePanel sizePanel;
    private SaveButton saveButton;
    private LoadButton loadButton;
    private SaveRemoteButton saveRemoteButton;
    private ReturnButton returnButton;
    private FilenamePanel filenamePanel;

    public Toolbar(Canvas canvas, DrawingPanel panel) {
        super();
        this.panel = panel;

        this.canvas = canvas;

        setBounds(padding + 800, padding, width - 3 * padding, height - 3 * padding);
        setLayout(null);
        setBackground(Color.white);

        int currentHeight = 0;
        colorPanel = new ColorPanel(this, currentHeight);
        currentHeight += ColorPanel.getCHeight();
        sizePanel = new SizePanel(this, currentHeight);
        currentHeight += SizePanel.getCHeight();
        saveButton = new SaveButton(this, "Save", currentHeight);
        currentHeight += SaveButton.height;
        loadButton = new LoadButton(this, "Load", currentHeight);
        currentHeight += LoadButton.height;
        saveRemoteButton = new SaveRemoteButton(this, "Save Remote", currentHeight);
        currentHeight += SaveRemoteButton.height;
        returnButton = new ReturnButton(this, "Back to My Galary", currentHeight);
        currentHeight += ReturnButton.height;
        filenamePanel = new FilenamePanel(this, currentHeight);
        currentHeight += FilenamePanel.getCHeight();

        add(colorPanel);
        add(sizePanel);
        add(saveButton);
        add(loadButton);
        add(saveRemoteButton);
        add(returnButton);
        add(filenamePanel);
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

    public void onSaveRemote() {
        canvas.saveAsImageRemote();
    }

    public void onLoad(String path) {
        canvas.loadImage(path);
    }

    public void onReturn() {
        panel.onReturn();
    }

    public void setFilename(String filename) {
        panel.filename = filename;
    }

    public String getFilename() {
        return panel.filename;
    }

    public static int getActualWidth() {
        return width - 3 * padding;
    }
}

package client.Toolbar;

import java.awt.*;
import javax.swing.*;

import client.Canvas.Canvas;
import client.Canvas.ColorFiller;
import client.Panel.DrawingPanel;
import client.Toolbar.Buttons.*;

public class Toolbar extends JPanel {
    private static final int width = 450;
    private static final int height = 700;
    private static final int padding = 30;

    private DrawingPanel panel;
    private int mode = Canvas.mode_pen;

    public String mainColor = Canvas.defaultMainColor;
    public String secondaryColor = Canvas.defaultSecondaryColor;
    public int size = Canvas.defaultSize;

    private ColorPanel colorPanel;
    private SizePanel sizePanel;
    private SaveButton saveButton;
    private SaveRemoteButton saveRemoteButton;
    private ReturnButton returnButton;
    private FilenamePanel filenamePanel;
    private PenModePanel penModePanel;
    private ShapePanel shapePanel;
    private ChooseColorPanel chooseColorPanel;

    public Toolbar(DrawingPanel panel) {
        super();
        this.panel = panel;

        setBounds(padding + 800, padding, width - 3 * padding, height - 3 * padding);
        setLayout(null);
        setBackground(Color.white);

        int currentHeight = 0;

        // color panel
        colorPanel = new ColorPanel(this, currentHeight);
        currentHeight += ColorPanel.getCHeight();

        // color chooser
        chooseColorPanel = new ChooseColorPanel(this, currentHeight);
        currentHeight += ChooseColorPanel.getCHeight();

        // size panel
        sizePanel = new SizePanel(this, currentHeight);
        currentHeight += SizePanel.getCHeight();

        // pen mode button
        penModePanel = new PenModePanel(this, currentHeight);
        currentHeight += PenModePanel.getCHeight();

        // shape panel
        shapePanel = new ShapePanel(this, currentHeight);
        currentHeight += ShapePanel.getCHeight();

        // save button
        saveButton = new SaveButton(this, "Save", currentHeight);
        currentHeight += SaveButton.height;

        // remote save button
        saveRemoteButton = new SaveRemoteButton(this, "Save Remote", currentHeight);
        currentHeight += SaveRemoteButton.height;

        // return button
        returnButton = new ReturnButton(this, "Back to My Galary", currentHeight);
        currentHeight += ReturnButton.height;

        // filename panel
        filenamePanel = new FilenamePanel(this, currentHeight);
        currentHeight += FilenamePanel.getCHeight();

        add(colorPanel);
        add(chooseColorPanel);
        add(sizePanel);
        add(penModePanel);
        add(shapePanel);
        add(saveButton);
        add(saveRemoteButton);
        add(returnButton);
        add(filenamePanel);
    }

    public void onMainColorChange(Color color) {
        onMainColorChange(ColorFiller.convertRGBHex(color.getRGB()));
    }

    public void onSecondaryColorChange(Color color) {
        onSecondaryColorChange(ColorFiller.convertRGBHex(color.getRGB()));
    }

    public void onMainColorChange(String color) {
        this.mainColor = color;
        panel.canvas.setMainColor(this.mainColor);
        colorPanel.repaint();
    }

    public void onSecondaryColorChange(String color) {
        this.secondaryColor = color;
        panel.canvas.setSecondaryColor(this.secondaryColor);
        colorPanel.repaint();
    }

    public void onSizeChange(int size) {
        this.size = size;
        panel.canvas.setSize(this.size);
    }

    public void onSaveLocal(String path) {
        panel.canvas.saveAsImage(path);
    }

    public void onSaveRemote() {
        panel.canvas.saveAsImageRemote();
    }

    public void onReturn() {
        panel.onReturn();
    }

    public void setMode(int mode, String... args) {
        this.mode = mode;
        if (this.mode == Canvas.mode_pen) {
            penModePanel.setFocus(mode);
            shapePanel.loseFocus();
        } else if (this.mode == Canvas.mode_shape) {
            penModePanel.loseFocus();
            shapePanel.setFocus(args[0]);
        }
        panel.canvas.setMode(mode, args);
    }

    public void chooseColor() {
        Color color = JColorChooser.showDialog(new Frame(), "Choose a color", Color.decode(mainColor));
        System.out.println(color.getRGB());
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

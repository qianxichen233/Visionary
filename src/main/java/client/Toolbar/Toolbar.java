package client.Toolbar;

import java.awt.*;
import javax.swing.*;

import client.Canvas.Canvas;
import client.Canvas.ColorFiller;
import client.Panel.DrawingPanel;
import client.Toolbar.Buttons.*;

public class Toolbar extends JPanel {
    public static final int width = 450;
    public static final int height = 700;
    public static final int padding = 30;

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
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // setBounds(padding + 800, padding, width - 3 * padding, height - 3 * padding);
        setBackground(Color.white);

        int currentHeight = 0;

        // Color divider
        add(new TextPanel(this, "Color", currentHeight));
        currentHeight += TextPanel.getCHeight();

        // color panel
        colorPanel = new ColorPanel(this, currentHeight);
        currentHeight += ColorPanel.getCHeight();
        add(colorPanel);

        // color chooser
        chooseColorPanel = new ChooseColorPanel(this, currentHeight);
        currentHeight += ChooseColorPanel.getCHeight();
        add(chooseColorPanel);

        // Size divider
        add(new TextPanel(this, "Size", currentHeight));
        currentHeight += TextPanel.getCHeight();

        // size panel
        sizePanel = new SizePanel(this, currentHeight);
        currentHeight += SizePanel.getCHeight();
        add(sizePanel);

        // Mode divider
        add(new TextPanel(this, "Mode", currentHeight));
        currentHeight += TextPanel.getCHeight();

        // pen mode button
        penModePanel = new PenModePanel(this, currentHeight);
        currentHeight += PenModePanel.getCHeight();
        add(penModePanel);

        // shape mode
        add(new TextPanel(this, "Shapes", currentHeight));
        currentHeight += TextPanel.getCHeight();

        // shape panel
        shapePanel = new ShapePanel(this, currentHeight);
        currentHeight += ShapePanel.getCHeight();
        add(shapePanel);

        // Operation divider
        add(new TextPanel(this, "Operation", currentHeight));
        currentHeight += TextPanel.getCHeight();

        // save button
        saveButton = new SaveButton(this, "Save To My Computer", currentHeight);
        currentHeight += SaveButton.height;
        add(saveButton);

        // remote save button
        saveRemoteButton = new SaveRemoteButton(this, "Save Remote", currentHeight);
        currentHeight += SaveRemoteButton.height;
        add(saveRemoteButton);

        // return button
        returnButton = new ReturnButton(this, "Back to My Galary", currentHeight);
        currentHeight += ReturnButton.height;
        add(returnButton);

        // Filename divider
        add(new TextPanel(this, "Filename", currentHeight));
        currentHeight += TextPanel.getCHeight();

        // filename panel
        filenamePanel = new FilenamePanel(this, currentHeight);
        currentHeight += FilenamePanel.getCHeight();
        add(filenamePanel);

        penModePanel.setFocus(mode);
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
        if (this.mode != Canvas.mode_shape) {
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

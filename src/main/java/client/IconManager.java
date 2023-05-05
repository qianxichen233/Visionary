package client;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import client.utils.MyUtils;

public class IconManager {
    private final static String basePath = System.getProperty("user.dir") + "/static/";

    public final static int defaultHeight = 50;
    public final static int defaultWidth = 50;

    public static BufferedImage getIcon(String name) {
        return getIcon(name, defaultWidth, defaultHeight);
    }

    public static BufferedImage getIcon(String name, int width, int height) {
        BufferedImage Icon = null;
        try {
            Icon = ImageIO.read(new File(basePath + name));
        } catch (Exception e) {
            System.out.println(e);
        }

        return MyUtils.resize(Icon, width, height);
    }
}

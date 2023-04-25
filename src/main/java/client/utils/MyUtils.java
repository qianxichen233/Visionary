package client.utils;

import java.awt.image.*;
import java.awt.*;

public class MyUtils {
    public static BufferedImage deepCopy(BufferedImage bi) {
        if (bi == null)
            return null;
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage resize(BufferedImage img, double ratio) {
        return resize(img, (int) (img.getWidth() * ratio), (int) (img.getHeight() * ratio));
    }
}

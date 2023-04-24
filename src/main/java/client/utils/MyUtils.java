package client.utils;

import java.awt.image.*;
import java.awt.image.BufferedImage;

public class MyUtils {
    public static BufferedImage deepCopy(BufferedImage bi) {
        if (bi == null)
            return null;
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}

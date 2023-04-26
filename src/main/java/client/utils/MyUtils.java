package client.utils;

import java.awt.image.*;
import java.awt.*;
import java.net.*;
import java.io.*;

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
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static BufferedImage resize(BufferedImage img, double ratio) {
        return resize(img, (int) (img.getWidth() * ratio), (int) (img.getHeight() * ratio));
    }

    public static void sendByteArray(Socket sock, byte[] bytes) {
        try {
            // DataInputStream dIn = new DataInputStream(sock.getInputStream());
            DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());

            // dIn.readByte();
            dOut.writeInt(bytes.length);
            dOut.write(bytes);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static byte[] receiveByteArray(Socket sock) {
        try {
            DataInputStream dIn = new DataInputStream(sock.getInputStream());
            // DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());

            // dOut.writeByte(0);
            int length = dIn.readInt();
            if (length > 0) {
                byte[] message = new byte[length];
                dIn.readFully(message, 0, message.length);
                return message;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }
}

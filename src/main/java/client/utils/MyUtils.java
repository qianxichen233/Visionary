package client.utils;

import java.awt.image.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import javax.crypto.spec.*;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.*;

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

    public static BufferedImage EncryptBufferedImage(BufferedImage image) {
        // image = toBufferedImage(toIntArray(image));
        // try {
        // File outputfile = new File("./3.png");
        // ImageIO.write(image, "png", outputfile);
        // } catch (Exception e) {
        // System.out.println(e);
        // }

        // KeyGenerator keyGen;
        // try {
        // keyGen = KeyGenerator.getInstance("AES");
        // SecureRandom secRandom = new SecureRandom();
        // keyGen.init(secRandom);
        // } catch (NoSuchAlgorithmException e) {
        // System.out.println(e);
        // return null;
        // }

        // Key key = keyGen.generateKey();
        // String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        // try {
        // PrintWriter out = new PrintWriter("key.txt");
        // out.println(encodedKey);
        // out.close();
        // } catch (Exception e) {
        // System.out.println(e);
        // return null;
        // }

        String encodedKey;
        try {
            Scanner in = new Scanner(new FileReader("key.txt"));
            encodedKey = in.nextLine();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        Key originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        byte[] imageArray = toIntArray(image);
        if (imageArray == null)
            System.out.println("image array null!");
        byte[] encryptedBytes = encryptPdfFile(originalKey, imageArray);
        System.out.println(imageArray.length);
        System.out.println(encryptedBytes.length);
        if (encryptedBytes == null)
            System.out.println("encrypted bytes null!");
        BufferedImage encryptedImage = toBufferedImage(encryptedBytes);
        try {
            File outputfile = new File("./1.png");
            ImageIO.write(encryptedImage, "png", outputfile);
        } catch (Exception e) {
            System.out.println(e);
        }
        DecryptBufferedImage(encryptedBytes);

        // BufferedImage encryptedImage = convertToBufferedimage(encryptedBytes);
        // if (encryptedImage == null)
        // System.out.println("encrypted image null!");

        return null;
    }

    public static BufferedImage DecryptBufferedImage(byte[] imageBytes) {
        String encodedKey;
        try {
            Scanner in = new Scanner(new FileReader("key.txt"));
            encodedKey = in.nextLine();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        Key originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        byte[] decryptedBytes = decryptPdfFile(originalKey, imageBytes);
        BufferedImage image = toBufferedImage(decryptedBytes);
        try {
            File outputfile = new File("./2.png");
            ImageIO.write(image, "png", outputfile);
        } catch (Exception e) {
            System.out.println("failed!");
            System.out.println(e);
        }
        return null;
    }

    public static BufferedImage toBufferedImage(byte[] pixels) {
        BufferedImage bi = new BufferedImage(710, 610, BufferedImage.TYPE_4BYTE_ABGR);
        final byte[] a = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        System.arraycopy(pixels, 0, a, 0, pixels.length);
        return bi;
    }

    public static byte[] toIntArray(BufferedImage image) {
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        return pixels;
    }

    private static byte[] encryptPdfFile(Key key, byte[] content) {
        Cipher cipher;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            try (FileOutputStream stream = new FileOutputStream("./iv.txt")) {
                stream.write(iv);
            }
            encrypted = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;

    }

    private static byte[] decryptPdfFile(Key key, byte[] textCryp) {
        Cipher cipher;
        byte[] decrypted = null;
        try {
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
            File file = new File("./iv.txt");
            byte[] iv = Files.readAllBytes(file.toPath());
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            decrypted = cipher.doFinal(textCryp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }

    private static byte[] convertToByteArray(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            System.out.println(e);
        }
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    private static BufferedImage convertToBufferedimage(byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }
}

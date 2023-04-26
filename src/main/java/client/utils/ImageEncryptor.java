package client.utils;

import java.awt.image.BufferedImage;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;

import javax.imageio.*;

public class ImageEncryptor {
    private static final int KEYLEN_BITS = 256;
    private static final int ITERATIONS = 65536;
    private static final int SALT_LEN = 8;

    private char[] password;
    private byte[] salt;

    public static class encryptedImage {
        BufferedImage image;
        byte[] iv;

        public encryptedImage(BufferedImage image, byte[] iv) {
            this.image = image;
            this.iv = iv;
        }
    }

    public ImageEncryptor(char[] password, byte[] salt) {
        this.password = password;
        this.salt = salt;
    }

    public BufferedImage encrypt(BufferedImage image) {
        BufferedImage result = null;

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEYLEN_BITS);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] imageBytes = toByteArray(image, "png");
            byte[] ciphertext = cipher.doFinal(imageBytes);
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }

    public static byte[] toByteArray(BufferedImage bi, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public static BufferedImage toBufferedImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;
    }
}

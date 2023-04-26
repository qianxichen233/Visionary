package client.utils;

import java.awt.image.BufferedImage;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.awt.image.*;

public class ImageEncryptor {
    private static final int KEYLEN_BITS = 256;
    private static final int ITERATIONS = 65536;
    private static final int SALT_LEN = 8;
    private static final String CIPHER_SUITE = "AES/CTR/NoPadding";

    private char[] password;
    private byte[] salt;
    private SecretKey secretKey;

    public static class EncryptedImage {
        public BufferedImage image;
        public byte[] iv;

        public EncryptedImage(BufferedImage image, byte[] iv) {
            this.image = image;
            this.iv = iv;
        }
    }

    public ImageEncryptor(char[] password, byte[] salt) {
        this.password = password;
        this.salt = salt;
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(this.password, this.salt, ITERATIONS, KEYLEN_BITS);
            SecretKey tmp = factory.generateSecret(spec);
            this.secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ImageEncryptor(String password, byte[] salt) {
        this(password.toCharArray(), salt);
    }

    public EncryptedImage encrypt(BufferedImage image) {
        BufferedImage result = null;
        byte[] iv;

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_SUITE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            AlgorithmParameters params = cipher.getParameters();
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] imageBytes = toByteArray(image);
            byte[] encryptedBytes = cipher.doFinal(imageBytes);
            result = toBufferedImage(encryptedBytes, image.getWidth(), image.getHeight());
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        return new EncryptedImage(result, iv);
    }

    public BufferedImage decrypt(EncryptedImage encryptedImage) {
        BufferedImage result = null;

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_SUITE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encryptedImage.iv));
            byte[] imageBytes = toByteArray(encryptedImage.image);
            byte[] decryptedBytes = cipher.doFinal(imageBytes);
            result = toBufferedImage(decryptedBytes, encryptedImage.image.getWidth(), encryptedImage.image.getHeight());
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }

    public static BufferedImage toBufferedImage(byte[] pixels, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        final byte[] a = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        System.arraycopy(pixels, 0, a, 0, pixels.length);
        return bi;
    }

    public static byte[] toByteArray(BufferedImage image) {
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        return pixels;
    }

    public static byte[] genSalt() {
        byte[] salt = new byte[SALT_LEN];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes(salt);
        return salt;
    }
}

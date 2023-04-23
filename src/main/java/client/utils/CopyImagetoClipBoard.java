package client.utils;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.*;
import java.io.*;

public class CopyImagetoClipBoard implements ClipboardOwner {
    public void copyImage(BufferedImage bi) {
        TransferableImage trans = new TransferableImage(bi);
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents(trans, this);
    }

    public static BufferedImage pasteImageFromClipboard() {
        java.awt.image.BufferedImage result;
        Image img;
        int width;
        int height;
        Graphics g;

        result = null;
        img = (Image) pasteFromClipboard(DataFlavor.imageFlavor);
        if (img != null) {
            width = img.getWidth(null);
            height = img.getHeight(null);
            result = new java.awt.image.BufferedImage(width, height,
                    java.awt.image.BufferedImage.TYPE_INT_RGB);
            g = result.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
        }

        return result;
    }

    public static Object pasteFromClipboard(DataFlavor flavor) {
        Clipboard clipboard;
        Object result;
        Transferable content;

        result = null;

        try {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            content = clipboard.getContents(null);
            if ((content != null)
                    && (content.isDataFlavorSupported(flavor)))
                result = content.getTransferData(flavor);
        } catch (Exception e) {
            result = null;
        }

        return result;
    }

    @Override
    public void lostOwnership(Clipboard clip, Transferable trans) {
        // System.out.println("Lost Clipboard Ownership");
    }

    private class TransferableImage implements Transferable {
        Image i;

        public TransferableImage(Image i) {
            this.i = i;
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException, IOException {
            if (flavor.equals(DataFlavor.imageFlavor) && i != null) {
                return i;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[1];
            flavors[0] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) {
                if (flavor.equals(flavors[i])) {
                    return true;
                }
            }

            return false;
        }
    }
}
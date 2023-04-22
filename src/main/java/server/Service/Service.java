package server.Service;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

import javax.imageio.ImageIO;

import server.DatabaseManager.DatabaseManager;
import server.DatabaseManager.Modal.Drawing;
import server.utils.*;

public class Service extends Thread {
    private final Socket sock;
    private final DatabaseManager databaseManager;

    public Service(Socket sock, DatabaseManager databaseManager) {
        this.sock = sock;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {
        try {
            Scanner sin = new Scanner(sock.getInputStream());
            PrintStream sout = new PrintStream(sock.getOutputStream());

            String type = sin.nextLine(); // whether login or register
            String username = sin.nextLine();
            String password = sin.nextLine();

            AccountHandler accountHandler = new AccountHandler(databaseManager);

            if (type.equals("login")) {
                if (!accountHandler.login(username, password)) {
                    sout.println("500");
                    sout.println("Wrong username or password!");
                    sock.close();
                    return;
                }
            } else if (type.equals("register")) {
                System.out.println("register!");
                if (!accountHandler.register(username, password)) {
                    sout.println("500");
                    sout.println("Username already exists!");
                } else
                    sout.println("200");
                sock.close();
                return;
            } else {
                sout.println("500");
                sout.println("Unknown operation!");
                sock.close();
                return;
            }
            // after login
            sout.println("200");

            while (true) {
                String operation = sin.nextLine();
                if (operation.equals("save")) {
                    String filename = sin.nextLine();
                    String hash = receiveImage(sock, System.getProperty("user.dir") + "/userImage/" + username);
                    sout.println(databaseManager.addDrawing(hash, username, filename));
                } else if (operation.equals("list")) {
                    ArrayList<Drawing> drawings = databaseManager.getUserDrawings(username);
                    sout.println(drawings.size());
                    for (Drawing drawing : drawings) {
                        sout.println(drawing.ID);
                        sout.println(drawing.filename);
                        sout.println(drawing.createdAt);
                    }
                } else if (operation.equals("get")) {
                    int ID = Integer.parseInt(sin.nextLine());
                    String hash = databaseManager.getDrawingHash(ID);
                    String path = System.getProperty("user.dir") + "/userImage/" + username + "/" + hash + ".png";
                    sendImage(sock, path);
                } else if (operation.equals("update")) {
                    int ID = Integer.parseInt(sin.nextLine());
                    String filename = sin.nextLine();
                    String previousHash = databaseManager.getDrawingHash(ID);
                    String newHash = receiveImage(sock, System.getProperty("user.dir") + "/userImage/" + username);
                    databaseManager.updateDrawing(ID, newHash, filename);
                    String path = System.getProperty("user.dir") + "/userImage/" + username + "/" + previousHash
                            + ".png";
                    File previousImage = new File(path);
                    previousImage.delete();
                }
            }

        } catch (Exception e) {
            System.out.println(sock.getInetAddress() + " closed");
        }
    }

    public String receiveImage(Socket sock, String path) {
        String imageHash = "";

        try {
            InputStream in = sock.getInputStream();

            byte[] b = new byte[30];
            int len = in.read(b);

            int filesize = Integer.parseInt(new String(b).substring(0, len));

            if (filesize > 0) {
                byte[] imgBytes = readExactly(in, filesize);
                imageHash = MyCrypto.SHAsum(imgBytes);

                Files.createDirectories(Paths.get(path));
                if (!path.endsWith("/"))
                    path += "/";
                FileOutputStream f = new FileOutputStream(path + imageHash + ".png");
                f.write(imgBytes);
                f.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return imageHash;
    }

    public void sendImage(Socket sock, String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            OutputStream out = sock.getOutputStream();

            ByteArrayOutputStream bScrn = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bScrn);
            byte imgBytes[] = bScrn.toByteArray();
            bScrn.close();

            out.write((Integer.toString(imgBytes.length)).getBytes());
            out.write(imgBytes, 0, imgBytes.length);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static byte[] readExactly(InputStream input, int size) throws IOException {
        byte[] data = new byte[size];
        int index = 0;
        while (index < size) {
            int bytesRead = input.read(data, index, size - index);
            if (bytesRead < 0) {
                throw new IOException("Insufficient data in stream");
            }
            index += bytesRead;
        }
        return data;
    }
}

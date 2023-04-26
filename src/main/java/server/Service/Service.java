package server.Service;

import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.*;
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

            AccountHandler accountHandler = new AccountHandler(databaseManager);

            String operation = sin.nextLine();
            if (operation.equals("login")) {
                String username = sin.nextLine();
                String password = sin.nextLine();
                String token = accountHandler.login(username, password);
                if (token == null) {
                    sout.println("500");
                    sin.nextLine();
                    sout.println("Wrong username or password!");
                } else {
                    sout.println("200");
                    sout.println(token);
                    sendByteArray(sock, databaseManager.getUserSalt(username));
                }
                sock.close();
                return;
            } else if (operation.equals("register")) {
                String username = sin.nextLine();
                String password = sin.nextLine();
                byte[] salt = receiveByteArray(sock);
                if (!accountHandler.register(username, password, salt)) {
                    sout.println("500");
                    sout.println("Username already exists!");
                } else
                    sout.println("200");
                sock.close();
                return;
            } else if (operation.equals("logout")) {
                String token = sin.nextLine();
                String username = accountHandler.getSession(token);
                if (username == null) {
                    sout.println("500");
                    sout.println("Invalid Token");
                } else {
                    sout.println("200");
                    accountHandler.logout(username);
                }
                sock.close();
                return;
            } else if (operation.equals("save")) {
                String token = sin.nextLine();
                String username = accountHandler.getSession(token);
                if (username == null) {
                    sout.println("500");
                    sout.println("Invalid Token");
                } else {
                    sout.println("200");
                    String filename = sin.nextLine();
                    String path = System.getProperty("user.dir") + "/userImage/" + username;
                    byte[] iv = receiveByteArray(sock);
                    byte[] thumb_iv = receiveByteArray(sock);
                    String hash = receiveImage(sock, path);
                    receiveImage(sock, path, hash + ".thumb");
                    sout.println(databaseManager.addDrawing(hash, username, filename, iv, thumb_iv));
                }
                sock.close();
                return;
            } else if (operation.equals("list")) {
                String token = sin.nextLine();
                String username = accountHandler.getSession(token);
                if (username == null) {
                    sout.println("500");
                    sout.println("Invalid Token");
                } else {
                    sout.println("200");
                    ArrayList<Drawing> drawings = databaseManager.getUserDrawings(username);
                    Collections.sort(drawings, new Comparator<Drawing>() {
                        @Override
                        public int compare(Drawing lhs, Drawing rhs) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                return dateFormat.parse(rhs.createdAt).compareTo(dateFormat.parse(lhs.createdAt));
                            } catch (Exception e) {
                                return 0;
                            }
                        }
                    });
                    sout.println(drawings.size());
                    for (Drawing drawing : drawings) {
                        sout.println(drawing.ID);
                        sout.println(drawing.filename);
                        sout.println(drawing.createdAt);
                        sin.nextLine();
                        sendByteArray(sock, drawing.thumb_iv);
                        String path = System.getProperty("user.dir") + "/userImage/" + username + "/" + drawing.hash
                                + ".thumb.png";
                        sendImage(sock, path);
                        int result = Integer.parseInt(sin.nextLine());
                        if (result == 500)
                            break;
                    }
                }
                sock.close();
                return;
            } else if (operation.equals("get")) {
                String token = sin.nextLine();
                String username = accountHandler.getSession(token);
                if (username == null) {
                    sout.println("500");
                    sout.println("Invalid Token");
                } else {
                    sout.println("200");
                    int ID = Integer.parseInt(sin.nextLine());
                    String hash = databaseManager.getDrawingHash(ID);
                    byte[] iv = databaseManager.getDrawingIV(ID);
                    sendByteArray(sock, iv);
                    String path = System.getProperty("user.dir") + "/userImage/" + username + "/" + hash + ".png";
                    sendImage(sock, path);
                }
                sock.close();
                return;
            } else if (operation.equals("update")) {
                String token = sin.nextLine();
                String username = accountHandler.getSession(token);
                if (username == null) {
                    sout.println("500");
                    sout.println("Invalid Token");
                } else {
                    sout.println("200");
                    int ID = Integer.parseInt(sin.nextLine());
                    String filename = sin.nextLine();
                    String previousHash = databaseManager.getDrawingHash(ID);
                    String path = System.getProperty("user.dir") + "/userImage/" + username;
                    byte[] iv = receiveByteArray(sock);
                    byte[] thumb_iv = receiveByteArray(sock);
                    String newHash = receiveImage(sock, path);
                    receiveImage(sock, path, newHash + ".thumb");
                    databaseManager.updateDrawing(ID, newHash, filename, iv, thumb_iv);
                    if (previousHash.equals(newHash))
                        return;
                    String oldpath = System.getProperty("user.dir") + "/userImage/" + username + "/" + previousHash;
                    File previousImage = new File(oldpath + ".png");
                    File previousThumb = new File(oldpath + ".thumb.png");
                    previousImage.delete();
                    previousThumb.delete();
                }
                sock.close();
                return;
            } else if (operation.equals("delete")) {
                String token = sin.nextLine();
                String username = accountHandler.getSession(token);
                if (username == null) {
                    sout.println("500");
                    sout.println("Invalid Token");
                } else {
                    sout.println("200");
                    int ID = Integer.parseInt(sin.nextLine());
                    String hash = databaseManager.getDrawingHash(ID);
                    if (!databaseManager.deleteDrawing(ID)) {
                        sout.println("500");
                        sock.close();
                        return;
                    }
                    String path = System.getProperty("user.dir") + "/userImage/" + username + "/" + hash;
                    File image = new File(path + ".png");
                    File thumb = new File(path + ".thumb.png");
                    image.delete();
                    thumb.delete();
                    sout.println("200");
                }
                sock.close();
                return;
            } else {
                sout.println("500");
                sout.println("Unknown operation!");
                sock.close();
                return;
            }
        } catch (Exception e) {
            System.out.println(sock.getInetAddress() + " closed");
        }
    }

    public String receiveImage(Socket sock, String path) {
        String imageHash = "";

        try {
            InputStream in = sock.getInputStream();
            OutputStream out = sock.getOutputStream();

            byte[] b = new byte[30];
            int len = in.read(b);
            out.write((byte) 0);

            int filesize = Integer.parseInt(new String(b).substring(0, len));

            if (filesize > 0) {
                byte[] imgBytes = readExactly(in, filesize);
                imageHash = MyCrypto.SHAsum(imgBytes);

                Files.createDirectories(Paths.get(path));
                if (!path.endsWith("/"))
                    path += "/";
                String fullpath = path + imageHash;
                FileOutputStream f = new FileOutputStream(fullpath + ".png");
                f.write(imgBytes);
                f.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return imageHash;
    }

    public void receiveImage(Socket sock, String path, String filename) {
        try {
            InputStream in = sock.getInputStream();
            OutputStream out = sock.getOutputStream();

            byte[] b = new byte[30];
            int len = in.read(b);
            out.write((byte) 0);

            int filesize = Integer.parseInt(new String(b).substring(0, len));

            if (filesize > 0) {
                byte[] imgBytes = readExactly(in, filesize);

                Files.createDirectories(Paths.get(path));
                if (!path.endsWith("/"))
                    path += "/";
                String fullpath = path + filename;
                FileOutputStream f = new FileOutputStream(fullpath + ".png");
                f.write(imgBytes);
                f.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void sendImage(Socket sock, String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            OutputStream out = sock.getOutputStream();
            InputStream in = sock.getInputStream();

            ByteArrayOutputStream bScrn = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bScrn);
            byte imgBytes[] = bScrn.toByteArray();
            bScrn.close();

            out.write((Integer.toString(imgBytes.length)).getBytes());
            in.read();
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

    public static void sendByteArray(Socket sock, byte[] bytes) throws IOException {
        // DataInputStream dIn = new DataInputStream(sock.getInputStream());
        DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());

        // dIn.readByte();
        dOut.writeInt(bytes.length);
        dOut.write(bytes);
    }

    public static byte[] receiveByteArray(Socket sock) throws IOException {
        DataInputStream dIn = new DataInputStream(sock.getInputStream());
        // DataOutputStream dOut = new DataOutputStream(sock.getOutputStream());

        // dOut.writeByte(0);
        int length = dIn.readInt();
        if (length > 0) {
            byte[] message = new byte[length];
            dIn.readFully(message, 0, message.length);
            return message;
        }
        return null;
    }
}

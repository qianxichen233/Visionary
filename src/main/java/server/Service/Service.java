package server.Service;

import java.net.*;
import java.io.*;
import java.util.*;

import server.DatabaseManager.DatabaseManager;
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
                    String hash = receiveImage(sock);
                    databaseManager.addDrawing(hash, username);
                }
            }

        } catch (Exception e) {
            System.out.println(sock.getInetAddress() + " closed");
        }
    }

    public String receiveImage(Socket sock) {
        String imageHash = "";

        try {
            InputStream in = sock.getInputStream();

            byte[] b = new byte[30];
            int len = in.read(b);

            int filesize = Integer.parseInt(new String(b).substring(0, len));

            if (filesize > 0) {
                byte[] imgBytes = readExactly(in, filesize);
                imageHash = MyCrypto.SHAsum(imgBytes);

                FileOutputStream f = new FileOutputStream(
                        System.getProperty("user.dir") + "/userImage/" + imageHash + ".png");
                f.write(imgBytes);
                f.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return imageHash;
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

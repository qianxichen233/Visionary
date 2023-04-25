package client;

import java.io.*;
import java.net.*;
import java.util.*;

public class SessionManager {
    private String IP;
    private int port;
    private String session;

    public static class MySock {
        public Socket sock;
        public Scanner sin;
        public PrintStream sout;

        public MySock(Socket sock, Scanner sin, PrintStream sout) {
            this.sock = sock;
            this.sin = sin;
            this.sout = sout;
        }

        public void close() {
            try {
                sin.close();
                sout.close();
                sock.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public SessionManager(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void clearSession() {
        this.session = null;
    }

    public MySock newSock(String operation, boolean withSession) {
        if (withSession && this.session == null)
            return null;

        Socket sock = null;
        Scanner sin = null;
        PrintStream sout = null;
        try {
            sock = new Socket(IP, port);
            sin = new Scanner(sock.getInputStream());
            sout = new PrintStream(sock.getOutputStream());

            sout.println(operation);
            if (withSession) {
                sout.println(session);
                int result = Integer.parseInt(sin.nextLine());
                if (result == 500) {
                    sock.close();
                    return null;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println(e);
            return null;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }

        return new MySock(sock, sin, sout);
    }
}

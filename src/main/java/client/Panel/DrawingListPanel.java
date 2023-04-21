package client.Panel;

import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import client.ClientInstance;
import client.Modal.Drawing;

public class DrawingListPanel extends MyPanel {
    private Socket sock;
    private String username;

    public ArrayList<Drawing> drawings = new ArrayList<Drawing>();
    public volatile boolean drawingFetched = false;

    private Header header;
    private DrawingList drawingList;

    public DrawingListPanel(ClientInstance client, Socket sock, String username) {
        super(client);
        this.sock = sock;
        this.username = username;

        header = new Header(this, this.username);
        header.setBounds(0, 0, jf.getWidth(), 50);

        drawingList = new DrawingList(this);
        drawingList.setBounds(0, 50, jf.getWidth(), jf.getHeight() - 50);

        jf.add(header);
        jf.add(drawingList);

        new getDrawings(this).start();

        jf.setLayout(null);
        jf.getContentPane().repaint();
        jf.setVisible(true);
    }

    public void clickDrawing(Drawing drawing) {
        try {
            PrintStream sout = new PrintStream(sock.getOutputStream());
            sout.println("get");
            sout.println(drawing.ID);
            client.drawingPage(receiveImage(sock), drawing.filename);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void logout() {
        try {
            sock.close();
            client.loginPage();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void render() {
        drawingList.render();
    }

    public Socket getSocket() {
        return sock;
    }

    private BufferedImage receiveImage(Socket sock) {
        BufferedImage image;
        try {
            InputStream in = sock.getInputStream();

            byte[] b = new byte[30];
            int len = in.read(b);

            int filesize = Integer.parseInt(new String(b).substring(0, len));

            if (filesize > 0) {
                byte[] imgBytes = readExactly(in, filesize);
                image = createImageFromBytes(imgBytes);
            } else
                return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

        return image;
    }

    private static byte[] readExactly(InputStream input, int size) throws IOException {
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

    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class Header extends JPanel {
    private String username;
    private DrawingListPanel panel;

    Header(DrawingListPanel panel, String username) {
        this.panel = panel;
        this.username = username;

        JPanel gapLeft = new JPanel();
        gapLeft.setSize(10, 0);
        JPanel gapRight = new JPanel();
        gapRight.setSize(10, 0);

        JPanel usernamePanel = new JPanel(new GridBagLayout());
        JLabel usernameText = new JLabel("Hello, " + this.username);
        usernamePanel.add(gapLeft);
        usernamePanel.add(usernameText);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        JLabel title = new JLabel("My Gallery");
        titlePanel.add(title);

        JPanel logoutPanel = new JPanel(new GridBagLayout());
        JButton logoutButton = new JButton("Log out");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Header.this.panel.logout();
            }
        });
        logoutPanel.add(logoutButton);
        logoutPanel.add(gapRight);

        setLayout(new BorderLayout());
        add(usernamePanel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.CENTER);
        add(logoutPanel, BorderLayout.EAST);
    }
}

class DrawingList extends JPanel {
    public static final int DrawingWidth = 200;
    public static final int DrawingHeight = 200;
    public static final int column = 4;

    private DrawingListPanel panel;

    public DrawingList(DrawingListPanel panel) {
        this.panel = panel;
    }

    public void render() {
        removeAll();
        invalidate();
        setBackground(Color.decode("#394e5e"));
        validate();
        repaint();

        if (!panel.drawingFetched) {
            JLabel loading = new JLabel("Loading...");
            loading.setFont(new Font(loading.getFont().getName(), Font.BOLD, 24));
            int width = (int) loading.getPreferredSize().getWidth();
            int height = (int) loading.getPreferredSize().getHeight();
            System.out.println(width);
            loading.setBounds((getWidth() - width) / 2, (getHeight() - height) / 2, width, 30);
            add(loading);
            return;
        }

        int gap = (getWidth() - column * DrawingWidth) / (column + 1);
        int curX = gap;
        int curY = 20;
        for (int i = 0; i < panel.drawings.size(); ++i) {
            if (i != 0 && i % column == 0) {
                curX = gap;
                curY += DrawingHeight + 20;
            }

            DrawingItem item = new DrawingItem(this, panel.drawings.get(i));
            item.setBounds(curX, curY, DrawingWidth, DrawingHeight);
            add(item);
            curX += DrawingWidth + gap;
        }
        repaint();
    }

    public void onClick(Drawing drawing) {
        panel.clickDrawing(drawing);
    }
}

class DrawingItem extends JPanel {
    private Drawing drawing;
    private DrawingList listPanel;

    private class onClickListener implements MouseListener {
        private Drawing drawing;

        public onClickListener(Drawing drawing) {
            this.drawing = drawing;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            listPanel.onClick(drawing);
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }
    }

    public DrawingItem(DrawingList listPanel, Drawing drawing) {
        this.drawing = drawing;
        this.listPanel = listPanel;
        addMouseListener(new onClickListener(drawing));
    }

    public DrawingItem(DrawingList listPanel) {
        this.listPanel = listPanel;
        JPanel titlePanel = new JPanel(new GridBagLayout());
        JLabel title = new JLabel("New Drawing");
        titlePanel.add(title);

        setLayout(new GridBagLayout());
        add(titlePanel);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.red);
        int width = g.getFontMetrics().stringWidth(drawing.filename);
        g.drawString(drawing.filename, (getWidth() - width) / 2, getHeight() / 3);
        width = g.getFontMetrics().stringWidth(drawing.createdAt);
        g.drawString(drawing.createdAt, (getWidth() - width) / 2, 2 * getHeight() / 3);
    }
}

class getDrawings extends Thread {
    DrawingListPanel panel;

    public getDrawings(DrawingListPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        panel.drawingFetched = false;
        panel.render();

        Socket sock = panel.getSocket();
        try {
            Scanner sin = new Scanner(sock.getInputStream());
            PrintStream sout = new PrintStream(sock.getOutputStream());
            sout.println("list");

            int number = Integer.parseInt(sin.nextLine());
            for (int i = 0; i < number; ++i) {
                int ID = Integer.parseInt(sin.nextLine());
                String filename = sin.nextLine();
                String createdAt = sin.nextLine();
                panel.drawings.add(new Drawing(ID, filename, createdAt));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        panel.drawingFetched = true;
        panel.render();
    }
}
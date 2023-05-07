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
import client.IconManager;
import client.SessionManager;
import client.Modal.Drawing;
import client.utils.ImageEncryptor;
import client.utils.MyUtils;

public class DrawingListPanel extends MyPanel {
    private SessionManager sessionManager;
    private ImageEncryptor imageEncryptor;
    private String username;

    public ArrayList<Drawing> drawings = new ArrayList<Drawing>();
    public volatile boolean drawingFetched = false;

    private Header header;
    private DrawingList drawingList;

    public DrawingListPanel(ClientInstance client, SessionManager sessionManager, ImageEncryptor imageEncryptor,
            String username) {
        super(client);
        this.sessionManager = sessionManager;
        this.imageEncryptor = imageEncryptor;
        this.username = username;

        header = new Header(this, this.username);
        header.setBounds(0, 0, jf.getWidth(), 50);

        drawingList = new DrawingList(this);
        JScrollPane scroller = new JScrollPane(drawingList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setBorder(null);
        drawingList.scroller = scroller;
        scroller.setBounds(0, 50, jf.getWidth(), jf.getHeight() - 50);

        jf.add(header);
        jf.add(scroller);

        new getDrawings(this).start();

        jf.setLayout(null);
        jf.getContentPane().repaint();
        jf.setVisible(true);
    }

    public void clickDrawing(Drawing drawing) {
        SessionManager.MySock mySock = sessionManager.newSock("get", true);
        if (mySock == null)
            return;
        mySock.sout.println(drawing.ID);
        byte[] iv = MyUtils.receiveByteArray(mySock.sock);
        client.drawingPage(imageEncryptor.decrypt(new ImageEncryptor.EncryptedImage(receiveImage(mySock.sock), iv)),
                drawing.filename, drawing.ID);
        mySock.close();
    }

    public void openDrawing() {
        FileDialog fd = new FileDialog(new Frame(), "Choose a file", FileDialog.LOAD);
        fd.setDirectory(System.getProperty("user.dir"));
        fd.setFilenameFilter(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png");
            }
        });
        fd.setVisible(true);
        if (fd.getFile() == null)
            return;
        String path = fd.getDirectory() + fd.getFile();
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        client.drawingPage(image, fd.getFile().substring(0, fd.getFile().lastIndexOf('.')));
    }

    public void deleteDrawing(Drawing drawing) {
        System.out.println("delete!");
        try {
            SessionManager.MySock mySock = sessionManager.newSock("delete", true);
            if (mySock == null)
                return;
            mySock.sout.println(drawing.ID);
            int result = Integer.parseInt(mySock.sin.nextLine());
            mySock.close();
            if (result == 200)
                new getDrawings(this).start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void newDrawing() {
        client.drawingPage();
    }

    public void logout() {
        SessionManager.MySock mySock = sessionManager.newSock("logout", true);
        if (mySock == null)
            return;
        sessionManager.clearSession();
        mySock.close();
        client.loginPage();
    }

    public void render() {
        drawingList.render();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public ImageEncryptor getImageEncryptor() {
        return imageEncryptor;
    }

    public static BufferedImage receiveImage(Socket sock) {
        BufferedImage image;
        try {
            InputStream in = sock.getInputStream();
            OutputStream out = sock.getOutputStream();

            byte[] b = new byte[30];
            int len = in.read(b);
            out.write((byte) 0);

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

    private static BufferedImage createImageFromBytes(byte[] imageData) {
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
        usernameText.setFont(new Font(usernameText.getFont().getFontName(), Font.PLAIN, 14));
        usernamePanel.add(gapLeft);
        usernamePanel.add(usernameText);

        JPanel titlePanel = new JPanel(new GridBagLayout());
        JLabel title = new JLabel("My Gallery");
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 18));
        titlePanel.add(title);

        JPanel logoutPanel = new JPanel(new GridBagLayout());
        JButton logoutButton = new JButton("Log out");
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
    public JScrollPane scroller;

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
            setLayout(null);
            JLabel loading = new JLabel("Loading...");
            loading.setFont(new Font(loading.getFont().getName(), Font.BOLD, 24));
            loading.setForeground(Color.white);
            int width = (int) loading.getPreferredSize().getWidth();
            int height = (int) loading.getPreferredSize().getHeight();
            loading.setBounds((getWidth() - width) / 2, (getHeight() - height) / 2, width, 30);
            add(loading);
            return;
        }

        int gap = (getWidth() - column * DrawingWidth) / (column + 1);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(getPlaceholder());

        JPanel itemRow = new JPanel();
        itemRow.setBackground(getBackground());
        itemRow.setPreferredSize(new Dimension(getWidth(), DrawingHeight));
        itemRow.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 1;
        c.weighty = 1;

        for (int j = 0; j < column; ++j) {
            c.gridx = j;
            if (j == column - 1)
                c.insets = new Insets(0, gap, 0, gap);
            else
                c.insets = new Insets(0, gap, 0, 0);

            if (j >= 2) {
                JPanel placeholder = new JPanel();
                placeholder.setPreferredSize(new Dimension(DrawingWidth, DrawingHeight));
                placeholder.setBackground(getBackground());
                itemRow.add(placeholder, c);
                continue;
            } else if (j == 1) {
                DrawingItem item = new DrawingItem(this, "new");
                item.setPreferredSize(new Dimension(DrawingWidth, DrawingHeight));
                itemRow.add(item, c);
            } else {
                DrawingItem item = new DrawingItem(this, "open");
                item.setPreferredSize(new Dimension(DrawingWidth, DrawingHeight));
                itemRow.add(item, c);
            }
        }
        add(itemRow);
        add(getPlaceholder());

        for (int i = 0; i < Math.ceil((double) panel.drawings.size() / column); ++i) {
            JPanel row = new JPanel();
            row.setBackground(getBackground());
            row.setPreferredSize(new Dimension(getWidth(), DrawingHeight));
            row.setLayout(new GridBagLayout());
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.weightx = 1;
            c.weighty = 1;
            for (int j = 0; j < column; ++j) {
                c.gridx = j;
                if (j == column - 1)
                    c.insets = new Insets(0, gap, 0, gap);
                else
                    c.insets = new Insets(0, gap, 0, 0);

                if (i * column + j >= panel.drawings.size()) {
                    JPanel placeholder = new JPanel();
                    placeholder.setPreferredSize(new Dimension(DrawingWidth, DrawingHeight));
                    placeholder.setBackground(getBackground());
                    row.add(placeholder, c);
                    continue;
                }

                DrawingItem item = new DrawingItem(this, panel.drawings.get(i * column + j));
                item.setPreferredSize(new Dimension(DrawingWidth, DrawingHeight));
                row.add(item, c);
            }

            add(row);
            add(getPlaceholder());
        }
        add(getPlaceholder());

        scroller.validate();
        scroller.repaint();
        validate();
        repaint();
    }

    public void onClick(Drawing drawing) {
        panel.clickDrawing(drawing);
    }

    public void onDelete(Drawing drawing) {
        panel.deleteDrawing(drawing);
    }

    public void openDrawing() {
        panel.openDrawing();
    }

    public void newDrawing() {
        panel.newDrawing();
    }

    public JPanel getPlaceholder() {
        JPanel placeholder = new JPanel();
        placeholder.setBackground(getBackground());
        placeholder.setPreferredSize(new Dimension(getWidth(), 20));
        return placeholder;
    }
}

class DrawingItem extends JPanel {
    private static final String newText = "New Drawing";
    private static final String openText = "Open Local Drawing";

    private Drawing drawing;
    private DrawingList listPanel;
    private String type;

    private class onClickListener implements MouseListener {
        private Drawing drawing;
        private String type;
        private boolean couldTrigger = false;

        public onClickListener(Drawing drawing) {
            this.drawing = drawing;
        }

        public onClickListener(String type) {
            this.type = type;
        }

        private void trigger() {
            if (drawing != null)
                listPanel.onClick(drawing);
            else if (type == "new")
                listPanel.newDrawing();
            else if (type == "open")
                listPanel.openDrawing();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            couldTrigger = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (couldTrigger)
                trigger();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            couldTrigger = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }
    }

    public DrawingItem(DrawingList listPanel, Drawing drawing) {
        this.drawing = drawing;
        this.listPanel = listPanel;
        addMouseListener(new onClickListener(drawing));
        final JButton deleteButton = new JButton("X");
        deleteButton.setBounds(5, 5, 20, 20);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawingItem.this.listPanel.onDelete(DrawingItem.this.drawing);
            }
        });
        add(deleteButton);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setLayout(null);
    }

    public DrawingItem(DrawingList listPanel, String type) {
        this.listPanel = listPanel;
        this.type = type;
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new onClickListener(this.type));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (drawing != null) {
            if (drawing.thumb != null) {
                int thumbWidth = drawing.thumb.getWidth();
                g.drawImage(drawing.thumb, (getWidth() - thumbWidth) / 2, 10, null);
            }
            g.setColor(Color.BLACK);
            Font old = g.getFont();
            g.setFont(new Font(g.getFont().getName(), Font.BOLD, 18));
            int width = g.getFontMetrics().stringWidth(drawing.filename);
            g.drawString(drawing.filename, (getWidth() - width) / 2, getHeight() - 40);
            g.setFont(old);
            width = g.getFontMetrics().stringWidth(drawing.createdAt);
            g.drawString(drawing.createdAt, (getWidth() - width) / 2, getHeight() - 10);
        } else if (type == "new") {
            try {
                BufferedImage Icon = IconManager.getIcon("add.png");
                g.drawImage(Icon, (getWidth() - IconManager.defaultWidth) / 2,
                        (getHeight() - IconManager.defaultHeight) / 2, null);
            } catch (Exception e) {
                System.out.println(e);
            }
            g.setColor(Color.BLACK);
            int width = g.getFontMetrics().stringWidth(newText);
            g.drawString(newText, (getWidth() - width) / 2, 3 * getHeight() / 4);
        } else if (type == "open") {
            try {
                BufferedImage Icon = IconManager.getIcon("folder.png");
                g.drawImage(Icon, (getWidth() - IconManager.defaultWidth) / 2,
                        (getHeight() - IconManager.defaultHeight) / 2, null);
            } catch (Exception e) {
                System.out.println(e);
            }
            g.setColor(Color.BLACK);
            int width = g.getFontMetrics().stringWidth(openText);
            g.drawString(openText, (getWidth() - width) / 2, 3 * getHeight() / 4);
        }
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

        SessionManager sessionManager = panel.getSessionManager();
        ImageEncryptor imageEncryptor = panel.getImageEncryptor();
        panel.drawings.clear();
        SessionManager.MySock mySock = sessionManager.newSock("list", true);
        if (mySock == null)
            return;

        int number = Integer.parseInt(mySock.sin.nextLine());
        for (int i = 0; i < number; ++i) {
            int ID = Integer.parseInt(mySock.sin.nextLine());
            String filename = mySock.sin.nextLine();
            String createdAt = mySock.sin.nextLine();
            mySock.sout.println("200");
            byte[] iv = MyUtils.receiveByteArray(mySock.sock);
            BufferedImage thumb = imageEncryptor
                    .decrypt(new ImageEncryptor.EncryptedImage(DrawingListPanel.receiveImage(mySock.sock), iv));
            panel.drawings.add(new Drawing(ID, filename, createdAt, thumb));
            mySock.sout.println("200");
        }
        mySock.close();
        panel.drawingFetched = true;
        panel.render();
    }
}
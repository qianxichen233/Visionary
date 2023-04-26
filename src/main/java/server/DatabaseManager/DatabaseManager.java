package server.DatabaseManager;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.util.Date;

import server.DatabaseManager.Modal.Drawing;

public class DatabaseManager {
    private static final String url = "jdbc:mariadb://127.0.0.1/";
    private static final String database = "Visionary";
    private static final String username = "root";
    private static final String password = "";

    private Connection conn = null;

    public DatabaseManager() {
        connect();
    }

    public DatabaseManager(boolean reset) {
        if (reset)
            resetDatabase();
        connect();
    }

    public DatabaseManager(Connection conn) {
        this.conn = conn;
    }

    private static void importSQL(Statement st, String path) throws SQLException, FileNotFoundException {
        File sqlfile = new File(path);

        InputStream in;
        Scanner s;
        in = new FileInputStream(sqlfile);
        s = new Scanner(in);

        s.useDelimiter("(;(\r)?\n)|(--\n)");
        try {
            while (s.hasNext()) {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/")) {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0) {
                    st.execute(line);
                }
            }
        } finally {
            s.close();
        }
    }

    private void resetDatabase() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, username, password);

            String sql = "CREATE DATABASE IF NOT EXISTS " + database;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            sql = "USE " + database;
            stmt.executeUpdate(sql);

            importSQL(stmt, "./src/sqls/clear.sql");
            importSQL(stmt, "./src/sqls/DDL.sql");
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void connect() {
        try {
            conn = DriverManager.getConnection(url + database, username, password);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean addUser(String username, String password, byte[] salt) {
        String sql = "INSERT INTO user VALUES(?, ?, ?)";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            s.setString(2, password);
            s.setBytes(3, salt);
            s.executeQuery();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public int addDrawing(String hash, String username, String filename, byte[] iv, byte[] thumb_iv) {
        String sql = "INSERT INTO drawing(hash, username, filename, createdAt, iv, thumb_iv) VALUES(?, ?, ?, ?, ?, ?)";
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int newID = -1;
        try {
            PreparedStatement s = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            s.setString(1, hash);
            s.setString(2, username);
            s.setString(3, filename);
            s.setString(4, now);
            s.setBytes(5, iv);
            s.setBytes(6, thumb_iv);
            s.executeQuery();
            ResultSet rs = s.getGeneratedKeys();
            rs.next();
            newID = rs.getInt(1);
        } catch (Exception e) {
            return -1;
        }
        return newID;
    }

    public boolean updateDrawing(int ID, String hash, String filename, byte[] iv, byte[] thumb_iv) {
        String sql = "UPDATE drawing SET hash = ?, filename = ?, createdAt = ?, iv = ?, thumb_iv = ? WHERE ID = ?";
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, hash);
            s.setString(2, filename);
            s.setString(3, now);
            s.setBytes(4, iv);
            s.setBytes(5, thumb_iv);
            s.setInt(6, ID);
            s.executeQuery();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getUserPassword(String username) {
        String password = null;
        String sql = "SELECT password FROM user WHERE username = ? LIMIT 1";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            s.executeQuery();
            ResultSet rs = s.executeQuery();
            if (rs.next())
                password = rs.getString("password");
        } catch (Exception e) {
            System.out.println(e);
        }

        return password;
    }

    public byte[] getUserSalt(String username) {
        byte[] salt = null;
        String sql = "SELECT key_salt FROM user WHERE username = ? LIMIT 1";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            s.executeQuery();
            ResultSet rs = s.executeQuery();
            if (rs.next())
                salt = rs.getBytes("key_salt");
        } catch (Exception e) {
            System.out.println(e);
        }

        return salt;
    }

    public ArrayList<Drawing> getUserDrawings(String username) {
        ArrayList<Drawing> result = new ArrayList<Drawing>();
        String sql = "SELECT * FROM drawing WHERE username = ?";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            s.executeQuery();
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                result.add(new Drawing(rs.getInt("ID"),
                        rs.getString("hash"),
                        rs.getString("username"),
                        rs.getString("filename"),
                        rs.getString("createdAt"),
                        rs.getBytes("iv"),
                        rs.getBytes("thumb_iv")));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }

    public String getDrawingHash(int ID) {
        String hash = "";
        String sql = "SELECT hash FROM drawing WHERE ID = ? LIMIT 1";

        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, ID);
            s.executeQuery();
            ResultSet rs = s.executeQuery();
            if (rs.next())
                hash = rs.getString("hash");
        } catch (Exception e) {
            System.out.println(e);
        }

        return hash;
    }

    public byte[] getDrawingIV(int ID) {
        byte[] iv = null;
        String sql = "SELECT iv FROM drawing WHERE ID = ? LIMIT 1";

        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, ID);
            s.executeQuery();
            ResultSet rs = s.executeQuery();
            if (rs.next())
                iv = rs.getBytes("iv");
        } catch (Exception e) {
            System.out.println(e);
        }

        return iv;
    }

    public boolean deleteDrawing(int ID) {
        String sql = "DELETE FROM drawing WHERE ID = ? LIMIT 1";

        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, ID);
            s.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean addSession(String token, String username, long expiration_days) {
        String sql = "INSERT INTO session VALUES(?, ?, ?)";
        long ltime = new Date().getTime() + expiration_days * 24 * 60 * 60 * 1000;
        String expiration_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(ltime));
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, token);
            s.setString(2, username);
            s.setString(3, expiration_date);
            s.executeQuery();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getSessionWithToken(String token) {
        String username = null;
        String sql = "SELECT username, expiration FROM session WHERE token = ? LIMIT 1";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, token);
            s.executeQuery();
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (dateFormat.parse(rs.getString("expiration")).compareTo(new Date()) < 0) {
                    clearSession(username);
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return username;
    }

    public String getSessionWithUsername(String username) {
        String token = null;
        String sql = "SELECT token, expiration FROM session WHERE username = ? LIMIT 1";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            s.executeQuery();
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                token = rs.getString("token");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (dateFormat.parse(rs.getString("expiration")).compareTo(new Date()) < 0) {
                    clearSession(username);
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return token;
    }

    public boolean clearSession(String username) {
        String sql = "DELETE FROM session WHERE username = ?";

        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            s.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public Connection getConnection() {
        return conn;
    }
}

package server.DatabaseManager;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.util.Date;

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

    public boolean addUser(String username, String password) {
        String sql = "INSERT INTO user VALUES(?, ?)";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            s.setString(2, password);
            s.executeQuery();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean addDrawing(String ID, String username) {
        String sql = "INSERT INTO drawing VALUES(?, ?, ?)";
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, ID);
            s.setString(2, username);
            s.setString(3, now);
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

    public ArrayList<String> getUserDrawings(String username) {
        ArrayList<String> result = new ArrayList<String>();
        String sql = "SELECT ID FROM drawing WHERE username = ?";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            s.executeQuery();
            ResultSet rs = s.executeQuery();
            while (rs.next())
                result.add(rs.getString("ID"));
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }

    public Connection getConnection() {
        return conn;
    }
}

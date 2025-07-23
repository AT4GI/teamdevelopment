package com.hotel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtil {
    private static final String DRIVER_NAME = "org.hsqldb.jdbcDriver";
    private static final String URL = "jdbc:hsqldb:hsql://localhost;shutdown=true";
    private static final String ID = "sa";
    private static final String PASSWORD = "";

    // データベース接続を取得
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            System.err.println("HSQLDB JDBC Driver not found. Please check your classpath.");
            throw new SQLException("JDBC Driver not found", e);
        }
        return DriverManager.getConnection(URL, ID, PASSWORD);
    }

    // リソースを閉じる
    public static void close(ResultSet rs, Statement stmt, Connection con) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
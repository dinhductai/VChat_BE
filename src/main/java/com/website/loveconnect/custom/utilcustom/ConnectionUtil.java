package com.website.loveconnect.custom.utilcustom;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
    private static String URL = "jdbc:mysql://localhost:3306/";
    private static String USER = "root";
    private static String PASSWORD = "mysqlcuatai123*";
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL,USER,PASSWORD);
            return conn;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
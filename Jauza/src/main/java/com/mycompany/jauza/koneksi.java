package com.mycompany.jauza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/db_jauza?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi ke database Laragon BERHASIL!");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Koneksi database GAGAL: " + e.getMessage());
        }
        return conn;
    }
}

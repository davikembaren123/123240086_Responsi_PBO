/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Davi
 */
package com.mycompany.sistemperpustakaan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class main {
    public static void main(String[] args) {
        // Jalankan aplikasi langsung ke layar login awal
        LoginFrame loginFrame = new LoginFrame();
        RegisterFrame registerFrame = new RegisterFrame();
        new AuthController(loginFrame, registerFrame);
        loginFrame.setVisible(true);
    }
}

// Koneksi ke MySQL XAMPP
class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/db_perpustakaan";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (Exception e) {
                System.err.println("Koneksi Gagal! Pastikan XAMPP MySQL hidup: " + e.getMessage());
            }
        }
        return connection;
    }
}
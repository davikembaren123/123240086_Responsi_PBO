/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemperpustakaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

// ==========================================
// CONTROLLER AKUN (LIMIT 3 PERCOBAAN)
// ==========================================
public class AuthController {
    private LoginFrame loginFrame; private RegisterFrame registerFrame; private int loginAttempts = 0;
    public AuthController(LoginFrame lf, RegisterFrame rf) {
        this.loginFrame = lf; this.registerFrame = rf;
        this.loginFrame.addLoginListener(e -> prosesLogin());
        this.loginFrame.addGoToRegisterListener(e -> { loginFrame.setVisible(false); registerFrame.setVisible(true); });
        this.registerFrame.addRegisterListener(e -> prosesRegister());
        this.registerFrame.addBackToLoginListener(e -> { registerFrame.setVisible(false); loginFrame.setVisible(true); });
    }
    private void prosesLogin() {
        String user = loginFrame.getUsername(); String pass = loginFrame.getPassword();
        if (user.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(loginFrame, "Input kosong!"); return; }
        boolean sukses = false;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM users WHERE username=? AND password=?")) {
            ps.setString(1, user); ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) sukses = true; }
        } catch (Exception ex) { ex.printStackTrace(); }
        if (sukses) {
            JOptionPane.showMessageDialog(loginFrame, "Selamat Datang!"); loginFrame.dispose();
            MainFrame mf = new MainFrame(); new ItemController(mf); mf.setVisible(true);
        } else {
            loginAttempts++; int sisa = 3 - loginAttempts;
            if (loginAttempts >= 3) { JOptionPane.showMessageDialog(loginFrame, "Gagal 3 kali. Aplikasi ditutup!"); System.exit(0); }
            else { JOptionPane.showMessageDialog(loginFrame, "Salah! Sisa percobaan: " + sisa); }
        }
    }
    private void prosesRegister() {
        String nama = registerFrame.getNamaLengkap(); String user = registerFrame.getUsername(); String pass = registerFrame.getPassword();
        if (nama.isEmpty() || user.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(registerFrame, "Isi semua kolom!"); return; }
        boolean kembar = false;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM users WHERE username=?")) {
            ps.setString(1, user);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) kembar = true; }
        } catch (Exception ex) { ex.printStackTrace(); }
        if (kembar) { JOptionPane.showMessageDialog(registerFrame, "Username sudah dipakai!"); return; }
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO users(nama_lengkap,username,password) VALUES(?,?,?)")) {
            ps.setString(1, nama); ps.setString(2, user); ps.setString(3, pass);
            if (ps.executeUpdate() > 0) { JOptionPane.showMessageDialog(registerFrame, "Sukses! Silakan login."); registerFrame.setVisible(false); loginFrame.setVisible(true); }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}

// ==========================================
// CONTROLLER LOGIKA CRUD UTAMA & INTERAKSI
// ==========================================
class ItemController {
    private MainFrame view;
    public ItemController(MainFrame view) { this.view = view; this.view.addTambahListener(e -> bukaFormTambah()); this.view.addEditListener(e -> bukaFormEdit()); this.view.addHapusListener(e -> prosesHapus()); this.view.addPinjamListener(e -> ubahStatus(1)); this.view.addKembalikanListener(e -> ubahStatus(0)); refreshTabel(); }
    private void refreshTabel() {
        DefaultTableModel model = view.getTableModel(); model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM items")) {
            while (rs.next()) {
                String jenis = rs.getString("jenis_item");
                String d1 = jenis.equals("Buku") ? rs.getString("pengarang") : rs.getString("edisi");
                String d2 = jenis.equals("Buku") ? rs.getString("isbn") : rs.getString("penerbit");
                String stat = rs.getInt("status_pinjam") == 1 ? "Dipinjam" : "Tersedia";
                model.addRow(new Object[]{rs.getInt("id"), jenis, rs.getString("judul"), rs.getInt("tahun_terbit"), d1, d2, stat});
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    private void bukaFormTambah() {
        FormDialog d = new FormDialog(view, "Tambah Baru");
        d.addSimpanListener(e -> {
            if (d.getJudulInput().isEmpty() || d.getTahunInput().isEmpty()) { JOptionPane.showMessageDialog(d, "Isi field!"); return; }
            String q = "INSERT INTO items (jenis_item, judul, tahun_terbit, pengarang, isbn, edisi, penerbit) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(q)) {
                ps.setString(1, d.getJenisSelected()); ps.setString(2, d.getJudulInput()); ps.setInt(3, Integer.parseInt(d.getTahunInput()));
                if (d.getJenisSelected().equals("Buku")) { ps.setString(4, d.getDetail1Input()); ps.setString(5, d.getDetail2Input()); ps.setNull(6, Types.VARCHAR); ps.setNull(7, Types.VARCHAR); }
                else { ps.setNull(4, Types.VARCHAR); ps.setNull(5, Types.VARCHAR); ps.setString(6, d.getDetail1Input()); ps.setString(7, d.getDetail2Input()); }
                if (ps.executeUpdate() > 0) { d.dispose(); refreshTabel(); }
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        d.setVisible(true);
    }
    private void bukaFormEdit() {
        int r = view.getTableItems().getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(view, "Pilih baris!"); return; }
        int id = (int) view.getTableModel().getValueAt(r, 0); String j = view.getTableModel().getValueAt(r, 1).toString();
        FormDialog d = new FormDialog(view, "Edit Data"); d.setFormValues(j, view.getTableModel().getValueAt(r, 2).toString(), view.getTableModel().getValueAt(r, 3).toString(), view.getTableModel().getValueAt(r, 4).toString(), view.getTableModel().getValueAt(r, 5).toString());
        d.addSimpanListener(e -> {
            String q = "UPDATE items SET judul=?, tahun_terbit=?, pengarang=?, isbn=?, edisi=?, penerbit=? WHERE id=?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(q)) {
                ps.setString(1, d.getJudulInput()); ps.setInt(2, Integer.parseInt(d.getTahunInput()));
                if (j.equals("Buku")) { ps.setString(3, d.getDetail1Input()); ps.setString(4, d.getDetail2Input()); ps.setNull(5, Types.VARCHAR); ps.setNull(6, Types.VARCHAR); }
                else { ps.setNull(3, Types.VARCHAR); ps.setNull(4, Types.VARCHAR); ps.setString(5, d.getDetail1Input()); ps.setString(6, d.getDetail2Input()); }
                ps.setInt(7, id); if (ps.executeUpdate() > 0) { d.dispose(); refreshTabel(); }
            } catch (Exception ex) { ex.printStackTrace(); }
        });
        d.setVisible(true);
    }
    private void prosesHapus() {
        int r = view.getTableItems().getSelectedRow(); if (r == -1) return;
        if (JOptionPane.showConfirmDialog(view, "Hapus data ini?", "Hapus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM items WHERE id=?")) {
                ps.setInt(1, (int) view.getTableModel().getValueAt(r, 0)); if (ps.executeUpdate() > 0) refreshTabel();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    private void ubahStatus(int status) {
        int r = view.getTableItems().getSelectedRow(); if (r == -1) return;
        if (view.getTableModel().getValueAt(r, 1).toString().equals("Majalah") && status == 1) { JOptionPane.showMessageDialog(view, "Majalah tidak bisa dipinjam!"); return; }
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement("UPDATE items SET status_pinjam=? WHERE id=?")) {
            ps.setInt(1, status); ps.setInt(2, (int) view.getTableModel().getValueAt(r, 0)); ps.executeUpdate(); refreshTabel();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
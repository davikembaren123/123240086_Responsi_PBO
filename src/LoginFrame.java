/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemperpustakaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

// ==========================================
// 1. HALAMAN LOGIN
// ==========================================
public class LoginFrame extends JFrame {
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnLogin = new JButton("Login");
    private JButton btnGoToRegister = new JButton("Belum punya akun? Daftar");

    public LoginFrame() {
        setTitle("Login - Sistem Perpustakaan Kota");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Username:"), gbc); gbc.gridx = 1; add(txtUsername, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Password:"), gbc); gbc.gridx = 1; add(txtPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; add(btnLogin, gbc); gbc.gridy = 3; add(btnGoToRegister, gbc);
    }
    public String getUsername() { return txtUsername.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public void addLoginListener(ActionListener l) { btnLogin.addActionListener(l); }
    public void addGoToRegisterListener(ActionListener l) { btnGoToRegister.addActionListener(l); }
}

// ==========================================
// 2. HALAMAN REGISTER
// ==========================================
class RegisterFrame extends JFrame {
    private JTextField txtNamaLengkap = new JTextField(20);
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JButton btnRegister = new JButton("Daftar Akun Baru");
    private JButton btnBackToLogin = new JButton("Kembali ke Login");

    public RegisterFrame() {
        setTitle("Register Akun Pustakawan");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Nama Lengkap:"), gbc); gbc.gridx = 1; add(txtNamaLengkap, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Username:"), gbc); gbc.gridx = 1; add(txtUsername, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Password:"), gbc); gbc.gridx = 1; add(txtPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; add(btnRegister, gbc); gbc.gridy = 4; add(btnBackToLogin, gbc);
    }
    public String getNamaLengkap() { return txtNamaLengkap.getText(); }
    public String getUsername() { return txtUsername.getText(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public void addRegisterListener(ActionListener l) { btnRegister.addActionListener(l); }
    public void addBackToLoginListener(ActionListener l) { btnBackToLogin.addActionListener(l); }
}

// ==========================================
// 3. HALAMAN DASHBOARD UTAMA
// ==========================================
class MainFrame extends JFrame {
    private JTable tableItems = new JTable();
    private DefaultTableModel tableModel;
    private JButton btnTambah = new JButton("Tambah Item");
    private JButton btnEdit = new JButton("Edit");
    private JButton btnHapus = new JButton("Hapus");
    private JButton btnPinjam = new JButton("Pinjam Buku");
    private JButton btnKembalikan = new JButton("Kembalikan");

    public MainFrame() {
        setTitle("Dashboard Manajemen Perpustakaan");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        String[] kolom = {"ID", "Jenis", "Judul", "Tahun Terbit", "Detail 1", "Detail 2", "Status"};
        tableModel = new DefaultTableModel(kolom, 0);
        tableItems.setModel(tableModel);
        add(new JScrollPane(tableItems), BorderLayout.CENTER);
        JPanel panelTombol = new JPanel();
        panelTombol.add(btnTambah); panelTombol.add(btnEdit); panelTombol.add(btnHapus);
        panelTombol.add(btnPinjam); panelTombol.add(btnKembalikan);
        add(panelTombol, BorderLayout.SOUTH);
    }
    public JTable getTableItems() { return tableItems; }
    public DefaultTableModel getTableModel() { return tableModel; }
    public void addTambahListener(ActionListener l) { btnTambah.addActionListener(l); }
    public void addEditListener(ActionListener l) { btnEdit.addActionListener(l); }
    public void addHapusListener(ActionListener l) { btnHapus.addActionListener(l); }
    public void addPinjamListener(ActionListener l) { btnPinjam.addActionListener(l); }
    public void addKembalikanListener(ActionListener l) { btnKembalikan.addActionListener(l); }
}

// ==========================================
// 4. DIALOG POP-UP TAMBAH / EDIT
// ==========================================
class FormDialog extends JDialog {
    private JComboBox<String> cbJenis = new JComboBox<>(new String[]{"Buku", "Majalah"});
    private JTextField txtJudul = new JTextField(20);
    private JTextField txtTahun = new JTextField(20);
    private JTextField txtDetail1 = new JTextField(20);
    private JTextField txtDetail2 = new JTextField(20);
    private JLabel lblDetail1 = new JLabel("Pengarang:");
    private JLabel lblDetail2 = new JLabel("ISBN:");
    private JButton btnSimpan = new JButton("Simpan Data");

    public FormDialog(Frame owner, String title) {
        super(owner, title, true); setSize(400, 300); setLocationRelativeTo(owner); setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(5, 5, 5, 5); gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Jenis:"), gbc); gbc.gridx = 1; add(cbJenis, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Judul:"), gbc); gbc.gridx = 1; add(txtJudul, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Tahun:"), gbc); gbc.gridx = 1; add(txtTahun, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(lblDetail1, gbc); gbc.gridx = 1; add(txtDetail1, gbc);
        gbc.gridx = 0; gbc.gridy = 4; add(lblDetail2, gbc); gbc.gridx = 1; add(txtDetail2, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; add(btnSimpan, gbc);
        cbJenis.addActionListener(e -> {
            if (cbJenis.getSelectedItem().toString().equals("Buku")) {
                lblDetail1.setText("Pengarang:"); lblDetail2.setText("ISBN:");
            } else {
                lblDetail1.setText("Edisi:"); lblDetail2.setText("Penerbit:");
            }
        });
    }
    public String getJenisSelected() { return cbJenis.getSelectedItem().toString(); }
    public String getJudulInput() { return txtJudul.getText(); }
    public String getTahunInput() { return txtTahun.getText(); }
    public String getDetail1Input() { return txtDetail1.getText(); }
    public String getDetail2Input() { return txtDetail2.getText(); }
    public void setFormValues(String j, String jd, String t, String d1, String d2) {
        cbJenis.setSelectedItem(j); txtJudul.setText(jd); txtTahun.setText(t); txtDetail1.setText(d1); txtDetail2.setText(d2); cbJenis.setEnabled(false);
    }
    public void addSimpanListener(ActionListener l) { btnSimpan.addActionListener(l); }
}
package com.mycompany.jauza;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class panelform extends javax.swing.JPanel {

    // Tetap memegang seluruh DAO untuk dioper ke Menu Utama
    private final AdminDAO adminDAO;
    private final PelangganDAO pelangganDAO;
    private final LayananDAO layananDAO;
    private final TransaksiDAO transaksiDAO;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public panelform(AdminDAO adminDAO, PelangganDAO pelangganDAO, LayananDAO layananDAO, TransaksiDAO transaksiDAO) {
        this.adminDAO = adminDAO;
        this.pelangganDAO = pelangganDAO;
        this.layananDAO = layananDAO;
        this.transaksiDAO = transaksiDAO;
        initModernUI();
    }

    private void initModernUI() {
        setPreferredSize(new Dimension(720, 430));
        setLayout(new GridLayout(1, 2)); // Split-screen 50% kiri, 50% kanan

        // ==========================================
        // 1. PANEL KIRI (BRANDING GRADASI BIRU)
        // ==========================================
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(0, 102, 204),
                    0, getHeight(), new Color(30, 58, 95)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setBorder(new EmptyBorder(40, 35, 40, 35));

        JPanel brandBox = new JPanel();
        brandBox.setOpaque(false);
        brandBox.setLayout(new BoxLayout(brandBox, BoxLayout.Y_AXIS));

        JLabel lblBadge = new JLabel("  SISTEM MANAJEMEN  ");
        lblBadge.setOpaque(true);
        lblBadge.setBackground(new Color(255, 255, 255, 40));
        lblBadge.setForeground(Color.WHITE);
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblBadge.setBorder(new EmptyBorder(5, 8, 5, 8));
        lblBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblAppTitle = new JLabel("JAUZA LAUNDRY");
        lblAppTitle.setForeground(Color.WHITE);
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblAppTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><div style='width:240px; color:#DCEBFF; line-height:1.4;'>"
                + "Kelola data pelanggan, layanan, transaksi cucian, dan cetak laporan keuangan dengan cepat dan terintegrasi."
                + "</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandBox.add(lblBadge);
        brandBox.add(Box.createVerticalStrut(14));
        brandBox.add(lblAppTitle);
        brandBox.add(Box.createVerticalStrut(10));
        brandBox.add(lblDesc);
        leftPanel.add(brandBox);

        // ==========================================
        // 2. PANEL KANAN (FORM LOGIN PUTIH BERSIH)
        // ==========================================
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(40, 42, 40, 42));

        JPanel formBox = new JPanel();
        formBox.setOpaque(false);
        formBox.setLayout(new BoxLayout(formBox, BoxLayout.Y_AXIS));
        formBox.setPreferredSize(new Dimension(275, 310));

        JLabel lblWelcome = new JLabel("Selamat Datang");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setForeground(new Color(30, 58, 95));
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Silakan masuk menggunakan akun admin");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(110, 120, 135));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Input Username
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(60, 70, 85));
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtUsername.setPreferredSize(new Dimension(275, 38));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername.putClientProperty("JTextField.placeholderText", "Masukkan username");
        txtUsername.putClientProperty("FlatLaf.style", "arc: 10; margin: 0,10,0,10");

        // Input Password (dengan tombol mata show/hide bawaan FlatLaf)
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(60, 70, 85));
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPassword.setPreferredSize(new Dimension(275, 38));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.putClientProperty("JTextField.placeholderText", "Masukkan password");
        txtPassword.putClientProperty("FlatLaf.style", "arc: 10; margin: 0,10,0,10; showRevealButton: true");

        // Tombol Login
        btnLogin = new JButton("Masuk ke Dashboard");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.setPreferredSize(new Dimension(275, 40));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.putClientProperty("FlatLaf.style",
                "arc: 10; borderWidth: 0; focusWidth: 0; "
                + "background: #0066CC; hoverBackground: #0052A3; pressedBackground: #003D7A; "
                + "foreground: #FFFFFF; font: bold +1");

        // Aksi tombol & tekan Enter
        btnLogin.addActionListener(e -> prosesLogin());
        txtUsername.addActionListener(e -> txtPassword.requestFocusInWindow());
        txtPassword.addActionListener(e -> prosesLogin());

        // Susun elemen form secara vertikal
        formBox.add(lblWelcome);
        formBox.add(Box.createVerticalStrut(4));
        formBox.add(lblSub);
        formBox.add(Box.createVerticalStrut(26));
        formBox.add(lblUser);
        formBox.add(Box.createVerticalStrut(6));
        formBox.add(txtUsername);
        formBox.add(Box.createVerticalStrut(16));
        formBox.add(lblPass);
        formBox.add(Box.createVerticalStrut(6));
        formBox.add(txtPassword);
        formBox.add(Box.createVerticalStrut(28));
        formBox.add(btnLogin);

        rightPanel.add(formBox);

        add(leftPanel);
        add(rightPanel);
    }

    private void prosesLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username dan Password tidak boleh kosong!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (adminDAO.login(username, password)) {
                javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
                new MenuUtama(pelangganDAO, layananDAO, transaksiDAO, adminDAO).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username atau Password salah!",
                        "Login Gagal", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocusInWindow();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi Error Database: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
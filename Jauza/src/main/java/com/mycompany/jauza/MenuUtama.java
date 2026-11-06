package com.mycompany.jauza;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuUtama extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MenuUtama() {
        // 1. Pengaturan Dasar Jendela
        setTitle("Sistem Manajemen Laundry - Jauza");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout()); 

        // 2. HEADER (Bagian Atas)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204)); 
        headerPanel.setPreferredSize(new Dimension(800, 60));

        JLabel titleLabel = new JLabel("Dasbor Admin Laundry");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH); 

        // 3. SIDEBAR (Menu Kiri)
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new GridLayout(7, 1, 10, 10)); 
        sidebarPanel.setBackground(Color.LIGHT_GRAY);
        sidebarPanel.setPreferredSize(new Dimension(200, 0)); 

        JButton btnDashboard = new JButton("Beranda");
        JButton btnPelanggan = new JButton("Kelola Pelanggan");
        JButton btnLayanan = new JButton("Kelola Layanan");
        JButton btnTransaksi = new JButton("Input Orderan");
        JButton btnLogout = new JButton("Logout");

        sidebarPanel.add(new JLabel("   Menu Navigasi :", SwingConstants.LEFT));
        sidebarPanel.add(btnDashboard);
        sidebarPanel.add(btnPelanggan);
        sidebarPanel.add(btnLayanan);
        sidebarPanel.add(btnTransaksi);
        sidebarPanel.add(new JLabel("")); 
        sidebarPanel.add(btnLogout);

        add(sidebarPanel, BorderLayout.WEST); 

        // 4. MAIN CONTENT (Bagian Tengah - Layar Utama)
        cardLayout = new CardLayout(); 
        mainPanel = new JPanel(cardLayout);

        JPanel pnlDashboard = new JPanel(new BorderLayout());
        JLabel lblWelcome = new JLabel("Selamat Datang di Sistem Laundry Jauza!", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.ITALIC, 18));
        pnlDashboard.add(lblWelcome, BorderLayout.CENTER);

        mainPanel.add(pnlDashboard, "dashboard");
        add(mainPanel, BorderLayout.CENTER); 

        // 5. FUNGSI TOMBOL (Aksi ketika diklik)
        
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(MenuUtama.this, 
                        "Yakin ingin keluar?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); 
                    
                    JFrame frame = new JFrame("Admin Login - Jauza");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.getContentPane().add(new panelform());
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            }
        });

        btnPelanggan.addActionListener(e -> JOptionPane.showMessageDialog(this, "Halaman Kelola Pelanggan belum dibuat."));
        btnLayanan.addActionListener(e -> JOptionPane.showMessageDialog(this, "Halaman Kelola Layanan belum dibuat."));
        btnTransaksi.addActionListener(e -> JOptionPane.showMessageDialog(this, "Halaman Input Orderan belum dibuat."));
        btnDashboard.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));
    } 
}
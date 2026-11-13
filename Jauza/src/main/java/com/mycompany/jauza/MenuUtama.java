package com.mycompany.jauza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuUtama extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private DefaultTableModel modelDashboard;
    private TransaksiDAO tDAO; // Disimpan agar bisa diakses oleh fungsi penyegaran dasbor

    public MenuUtama(PelangganDAO pDAO, LayananDAO lDAO, TransaksiDAO tDAO, AdminDAO aDAO) {
        this.tDAO = tDAO;
        
        setTitle("Sistem Manajemen Laundry - Jauza");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout()); 

        // HEADER
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204)); 
        headerPanel.setPreferredSize(new Dimension(800, 60));

        JLabel titleLabel = new JLabel("Dasbor Admin Laundry");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH); 

        // SIDEBAR
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

        cardLayout = new CardLayout(); 
        mainPanel = new JPanel(cardLayout);

        // --- DASHBOARD (BERANDA) ---
        JPanel pnlDashboard = new JPanel(new BorderLayout(10, 10));
        pnlDashboard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblWelcome = new JLabel("Daftar Cucian Berjalan (On Air)", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        pnlDashboard.add(lblWelcome, BorderLayout.NORTH);

        modelDashboard = new DefaultTableModel(new String[]{"ID Order", "Pelanggan", "Layanan", "Tanggal Masuk", "Berat", "Total Tagihan", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabelDashboard = new JTable(modelDashboard);
        pnlDashboard.add(new JScrollPane(tabelDashboard), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Segarkan Data");
        btnRefresh.addActionListener(e -> loadDataDashboard());
        
        JPanel pnlBawahDashboard = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBawahDashboard.add(btnRefresh);
        pnlDashboard.add(pnlBawahDashboard, BorderLayout.SOUTH);

        // Muat data beranda untuk pertama kali
        loadDataDashboard();

        // MENDAFTARKAN HALAMAN KE MEMORI
        mainPanel.add(pnlDashboard, "dashboard");
        mainPanel.add(new FormPelanggan(pDAO), "pelanggan"); 
        mainPanel.add(new FormLayanan(lDAO), "layanan");
        mainPanel.add(new FormTransaksi(tDAO, pDAO, lDAO), "transaksi");
        
        add(mainPanel, BorderLayout.CENTER); 

        // FUNGSI TOMBOL
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(MenuUtama.this, 
                        "Yakin ingin keluar?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose(); 
                    JFrame frame = new JFrame("Admin Login - Jauza");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.getContentPane().add(new panelform(aDAO, pDAO, lDAO, tDAO));
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            }
        });

        btnDashboard.addActionListener(e -> {
            loadDataDashboard(); // Otomatis refresh data saat menu Beranda diklik
            cardLayout.show(mainPanel, "dashboard");
        });
        btnPelanggan.addActionListener(e -> cardLayout.show(mainPanel, "pelanggan")); 
        btnLayanan.addActionListener(e -> cardLayout.show(mainPanel, "layanan"));
        btnTransaksi.addActionListener(e -> cardLayout.show(mainPanel, "transaksi")); 
    } 
    
    // Fungsi khusus untuk menarik data cucian yang sedang berjalan ke Dasbor
    private void loadDataDashboard() {
        modelDashboard.setRowCount(0);
        for (Transaksi t : tDAO.ambilSemuaData()) {
            // Filter: Hanya tampilkan cucian yang statusnya Antrean atau Diproses
            if (t.getStatusCucian().equalsIgnoreCase("Antrean") || t.getStatusCucian().equalsIgnoreCase("Diproses")) {
                modelDashboard.addRow(new Object[]{
                    t.getIdTransaksi(), 
                    t.getNamaPelanggan(), 
                    t.getNamaLayanan(),
                    t.getTanggalMasuk(), 
                    t.getBeratKg() + " Kg", 
                    "Rp " + t.getTotalHarga(), 
                    t.getStatusCucian()
                });
            }
        }
    }
}
package com.mycompany.jauza;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MenuUtama extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private DefaultTableModel modelDashboard;
    private final TransaksiDAO tDAO;

    // Komponen UI Dinamis
    private JLabel lblHeaderTitle;
    private JLabel lblStatAktif;
    private JLabel lblStatTotalOrder;
    private JLabel lblStatPendapatan;
    private JButton[] navButtons;

    private final DecimalFormat rupiahFormat = new DecimalFormat("Rp #,##0");

    public MenuUtama(PelangganDAO pDAO, LayananDAO lDAO, TransaksiDAO tDAO, AdminDAO aDAO) {
        this.tDAO = tDAO;

        setTitle("Sistem Manajemen Laundry - Jauza");
        setSize(1120, 680);
        setMinimumSize(new Dimension(960, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ==========================================
        // 1. SIDEBAR NAVIGASI (NAVY #1E3A5F)
        // ==========================================
        JPanel sidebarPanel = new JPanel(new BorderLayout());
        sidebarPanel.setBackground(new Color(30, 58, 95));
        sidebarPanel.setPreferredSize(new Dimension(235, 0));

        // Branding Atas Sidebar
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(new EmptyBorder(24, 22, 24, 22));

        JLabel lblBrand = new JLabel("JAUZA LAUNDRY");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblBrand.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel("Management System");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(176, 204, 238));

        brandPanel.add(lblBrand);
        brandPanel.add(Box.createVerticalStrut(3));
        brandPanel.add(lblSubtitle);

        // Daftar Tombol Menu
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel lblMenuTitle = new JLabel("MENU UTAMA");
        lblMenuTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblMenuTitle.setForeground(new Color(148, 178, 214));
        lblMenuTitle.setBorder(new EmptyBorder(0, 10, 10, 0));
        lblMenuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnDashboard = createNavButton("Beranda");
        JButton btnPelanggan = createNavButton("Kelola Pelanggan");
        JButton btnLayanan = createNavButton("Kelola Layanan");
        JButton btnTransaksi = createNavButton("Input Orderan");

        navButtons = new JButton[]{btnDashboard, btnPelanggan, btnLayanan, btnTransaksi};

        menuPanel.add(lblMenuTitle);
        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createVerticalStrut(6));
        menuPanel.add(btnPelanggan);
        menuPanel.add(Box.createVerticalStrut(6));
        menuPanel.add(btnLayanan);
        menuPanel.add(Box.createVerticalStrut(6));
        menuPanel.add(btnTransaksi);

        // Tombol Logout di Bagian Bawah Sidebar
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setOpaque(false);
        logoutPanel.setBorder(new EmptyBorder(14, 14, 20, 14));

        JButton btnLogout = new JButton("Keluar / Logout");
        btnLogout.setPreferredSize(new Dimension(200, 40));
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.putClientProperty("FlatLaf.style",
                "arc: 10; borderWidth: 0; focusWidth: 0; "
                + "background: #DC2626; hoverBackground: #B91C1C; pressedBackground: #991B1B; "
                + "foreground: #FFFFFF; font: bold");
        logoutPanel.add(btnLogout, BorderLayout.CENTER);

        sidebarPanel.add(brandPanel, BorderLayout.NORTH);
        sidebarPanel.add(menuPanel, BorderLayout.CENTER);
        sidebarPanel.add(logoutPanel, BorderLayout.SOUTH);
        add(sidebarPanel, BorderLayout.WEST);

        // ==========================================
        // 2. AREA KANAN (HEADER + KONTEN UTAMA)
        // ==========================================
        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setBackground(new Color(244, 247, 251));

        // Top Header Bar Putih
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(0, 64));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)),
                new EmptyBorder(0, 26, 0, 26)
        ));

        lblHeaderTitle = new JLabel("Beranda Dashboard");
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblHeaderTitle.setForeground(new Color(30, 58, 95));

        String tanggalHariIni = new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date());
        JLabel lblRightInfo = new JLabel("Admin  •  " + tanggalHariIni);
        lblRightInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRightInfo.setForeground(new Color(100, 116, 139));

        headerPanel.add(lblHeaderTitle, BorderLayout.WEST);
        headerPanel.add(lblRightInfo, BorderLayout.EAST);
        rightContainer.add(headerPanel, BorderLayout.NORTH);

        // CardLayout Konten Utama
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(244, 247, 251));

        // --- HALAMAN DASHBOARD (BERANDA) ---
        JPanel pnlDashboard = buildDashboardPanel();

        // Daftarkan semua halaman ke memori
        mainPanel.add(pnlDashboard, "dashboard");
        mainPanel.add(new FormPelanggan(pDAO), "pelanggan");
        mainPanel.add(new FormLayanan(lDAO), "layanan");
        mainPanel.add(new FormTransaksi(tDAO, pDAO, lDAO), "transaksi");

        rightContainer.add(mainPanel, BorderLayout.CENTER);
        add(rightContainer, BorderLayout.CENTER);

        // Set menu awal aktif & muat data pertama kali
        setActiveNavButton(btnDashboard);
        loadDataDashboard();

        // ==========================================
        // 3. AKSI TOMBOL NAVIGASI & LOGOUT
        // ==========================================
        btnDashboard.addActionListener(e -> {
            setActiveNavButton(btnDashboard);
            lblHeaderTitle.setText("Beranda Dashboard");
            loadDataDashboard();
            cardLayout.show(mainPanel, "dashboard");
        });

        btnPelanggan.addActionListener(e -> {
            setActiveNavButton(btnPelanggan);
            lblHeaderTitle.setText("Kelola Data Pelanggan");
            cardLayout.show(mainPanel, "pelanggan");
        });

        btnLayanan.addActionListener(e -> {
            setActiveNavButton(btnLayanan);
            lblHeaderTitle.setText("Kelola Jenis Layanan");
            cardLayout.show(mainPanel, "layanan");
        });

        btnTransaksi.addActionListener(e -> {
            setActiveNavButton(btnTransaksi);
            lblHeaderTitle.setText("Input & Kelola Orderan");
            cardLayout.show(mainPanel, "transaksi");
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    MenuUtama.this,
                    "Yakin ingin keluar dari sesi admin?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                JFrame frame = new JFrame("Admin Login - Jauza");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new panelform(aDAO, pDAO, lDAO, tDAO));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    // Membangun tata letak Dashboard (Kartu Statistik + Tabel + Tombol Laporan)
    private JPanel buildDashboardPanel() {
        JPanel pnlDashboard = new JPanel(new BorderLayout(0, 18));
        pnlDashboard.setBackground(new Color(244, 247, 251));
        pnlDashboard.setBorder(new EmptyBorder(22, 26, 22, 26));

        // 1. Baris 3 Kartu Statistik
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 18, 0));
        statsPanel.setOpaque(false);

        lblStatAktif = new JLabel("0 Order");
        lblStatTotalOrder = new JLabel("0 Transaksi");
        lblStatPendapatan = new JLabel("Rp 0");

        statsPanel.add(createStatCard("Cucian Berjalan (On Air)", lblStatAktif, new Color(0, 102, 204)));
        statsPanel.add(createStatCard("Total Semua Orderan", lblStatTotalOrder, new Color(30, 58, 95)));
        statsPanel.add(createStatCard("Total Pendapatan", lblStatPendapatan, new Color(15, 118, 110)));

        pnlDashboard.add(statsPanel, BorderLayout.NORTH);

        // 2. Kartu Tabel Cucian Berjalan
        JPanel tableCard = new JPanel(new BorderLayout(0, 12));
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new EmptyBorder(18, 20, 18, 20));
        tableCard.putClientProperty("FlatLaf.style", "arc: 16");

        JPanel tableHeaderBar = new JPanel(new BorderLayout());
        tableHeaderBar.setOpaque(false);

        JLabel lblTableTitle = new JLabel("Daftar Cucian Berjalan (Antrean & Diproses)");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTableTitle.setForeground(new Color(30, 41, 59));

        JButton btnRefresh = new JButton("Segarkan Data");
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.putClientProperty("FlatLaf.style",
                "arc: 8; borderWidth: 1; borderColor: #CBD5E1; "
                + "background: #F8FAFC; hoverBackground: #E2E8F0; foreground: #334155");
        btnRefresh.addActionListener(e -> loadDataDashboard());

        tableHeaderBar.add(lblTableTitle, BorderLayout.WEST);
        tableHeaderBar.add(btnRefresh, BorderLayout.EAST);

        // Konfigurasi Tabel Modern
        modelDashboard = new DefaultTableModel(
                new String[]{"ID Order", "Pelanggan", "Layanan", "Tanggal Masuk", "Berat", "Total Tagihan", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabelDashboard = new JTable(modelDashboard);
        styleModernTable(tabelDashboard);

        JScrollPane scrollPane = new JScrollPane(tabelDashboard);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // 3. Baris Tombol Laporan (JasperReports)
        JButton btnCetakLaporan = new JButton("Cetak Laporan");
        btnCetakLaporan.setPreferredSize(new Dimension(145, 38));
        btnCetakLaporan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCetakLaporan.putClientProperty("FlatLaf.style",
                "arc: 10; borderWidth: 0; focusWidth: 0; "
                + "background: #0066CC; hoverBackground: #0052A3; pressedBackground: #003D7A; "
                + "foreground: #FFFFFF; font: bold");
        btnCetakLaporan.addActionListener(e -> ReportManager.tampilkanLaporan());

        JButton btnExportPDF = new JButton("Ekspor PDF");
        btnExportPDF.setPreferredSize(new Dimension(130, 38));
        btnExportPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportPDF.putClientProperty("FlatLaf.style",
                "arc: 10; borderWidth: 0; focusWidth: 0; "
                + "background: #0F766E; hoverBackground: #115E59; pressedBackground: #134E4A; "
                + "foreground: #FFFFFF; font: bold");
        btnExportPDF.addActionListener(e -> ReportManager.eksporKePDF(MenuUtama.this));

        JPanel pnlBawahDashboard = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBawahDashboard.setOpaque(false);
        pnlBawahDashboard.setBorder(new EmptyBorder(6, 0, 0, 0));
        pnlBawahDashboard.add(btnExportPDF);
        pnlBawahDashboard.add(btnCetakLaporan);

        tableCard.add(tableHeaderBar, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        tableCard.add(pnlBawahDashboard, BorderLayout.SOUTH);

        pnlDashboard.add(tableCard, BorderLayout.CENTER);
        return pnlDashboard;
    }

    // Helper: Membuat Kartu Statistik
    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor),
                new EmptyBorder(16, 18, 16, 18)
        ));
        card.putClientProperty("FlatLaf.style", "arc: 12");

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(100, 116, 139));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(new Color(15, 23, 42));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // Helper: Membuat Tombol Sidebar
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setPreferredSize(new Dimension(205, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Helper: Menandai Menu Sidebar yang Sedang Aktif
    private void setActiveNavButton(JButton activeBtn) {
        for (JButton btn : navButtons) {
            if (btn == activeBtn) {
                btn.putClientProperty("FlatLaf.style",
                        "arc: 10; borderWidth: 0; focusWidth: 0; margin: 0,14,0,14; "
                        + "background: #0066CC; hoverBackground: #0066CC; "
                        + "foreground: #FFFFFF; font: bold");
            } else {
                btn.putClientProperty("FlatLaf.style",
                        "arc: 10; borderWidth: 0; focusWidth: 0; margin: 0,14,0,14; "
                        + "background: #1E3A5F; hoverBackground: #294B77; pressedBackground: #172D4B; "
                        + "foreground: #CBD5E1; font: plain");
            }
        }
    }

    // Helper: Merapikan Tampilan JTable
    private void styleModernTable(JTable table) {
        table.setRowHeight(34);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(241, 245, 249));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(new Color(30, 58, 95));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(new Color(51, 65, 85));
        header.setPreferredSize(new Dimension(0, 36));
        header.setReorderingAllowed(false);

        // Renderer kustom untuk Zebra Striping, Alignment, dan Warna Status
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(new Color(30, 41, 59));
                }

                // Alignment per kolom
                if (column == 0 || column == 3 || column == 4 || column == 6) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 5) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                // Warna khusus kolom Status
                if (column == 6 && value != null && !isSelected) {
                    String status = value.toString();
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    if (status.equalsIgnoreCase("Antrean")) {
                        c.setForeground(new Color(217, 119, 6)); // Oranye
                    } else if (status.equalsIgnoreCase("Diproses")) {
                        c.setForeground(new Color(0, 102, 204)); // Biru
                    }
                } else {
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        // Lebar kolom proporsional
        table.getColumnModel().getColumn(0).setPreferredWidth(65);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(75);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(95);
    }

    // Menarik data cucian berjalan ke tabel + memperbarui 3 kartu statistik
    private void loadDataDashboard() {
        modelDashboard.setRowCount(0);
        List<Transaksi> semuaData = tDAO.ambilSemuaData();

        int jumlahAktif = 0;
        double totalPendapatan = 0;

        for (Transaksi t : semuaData) {
            totalPendapatan += t.getTotalHarga();

            if (t.getStatusCucian().equalsIgnoreCase("Antrean") || t.getStatusCucian().equalsIgnoreCase("Diproses")) {
                jumlahAktif++;
                modelDashboard.addRow(new Object[]{
                    t.getIdTransaksi(),
                    t.getNamaPelanggan(),
                    t.getNamaLayanan(),
                    t.getTanggalMasuk(),
                    t.getBeratKg() + " Kg",
                    rupiahFormat.format(t.getTotalHarga()),
                    t.getStatusCucian()
                });
            }
        }

        lblStatAktif.setText(jumlahAktif + " Order");
        lblStatTotalOrder.setText(semuaData.size() + " Transaksi");
        lblStatPendapatan.setText(rupiahFormat.format(totalPendapatan));
    }
}
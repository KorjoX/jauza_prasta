package com.mycompany.jauza;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

public class FormTransaksi extends JPanel {

    private JComboBox<String> cbPelanggan, cbLayanan, cbStatus;
    private JTextField txtTanggal, txtBerat, txtTotal, txtCari;
    private JButton btnHitung, btnSimpan, btnUbah, btnHapus, btnBatal, btnCetakNota;
    private JTable tabelTransaksi;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel lblJumlahData;
    private int idTerpilih = 0;

    private final TransaksiDAO transaksiDAO;
    private final PelangganDAO pelangganDAO;
    private final LayananDAO layananDAO;
    private final DecimalFormat rupiahFormat = new DecimalFormat("Rp #,##0");

    public FormTransaksi(TransaksiDAO transaksiDAO, PelangganDAO pelangganDAO, LayananDAO layananDAO) {
        this.transaksiDAO = transaksiDAO;
        this.pelangganDAO = pelangganDAO;
        this.layananDAO = layananDAO;

        setLayout(new BorderLayout(0, 18));
        setBackground(new Color(244, 247, 251));
        setBorder(new EmptyBorder(22, 26, 22, 26));

        // ==========================================
        // 1. KARTU FORM INPUT ORDERAN (ATAS)
        // ==========================================
        JPanel formCard = new JPanel(new BorderLayout(0, 14));
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(new EmptyBorder(18, 22, 18, 22));
        formCard.putClientProperty("FlatLaf.style", "arc: 16");

        JLabel lblFormTitle = new JLabel("Input & Kelola Orderan Cucian");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormTitle.setForeground(new Color(30, 41, 59));
        formCard.add(lblFormTitle, BorderLayout.NORTH);

        JPanel inputGrid = new JPanel(new GridBagLayout());
        inputGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Baris 1: Pelanggan | Layanan | Status
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0.34; gbc.insets = new Insets(4, 0, 6, 14);
        inputGrid.add(createFieldLabel("Pilih Pelanggan"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.38; gbc.insets = new Insets(4, 0, 6, 14);
        inputGrid.add(createFieldLabel("Pilih Layanan"), gbc);

        gbc.gridx = 2; gbc.weightx = 0.28; gbc.insets = new Insets(4, 0, 6, 0);
        inputGrid.add(createFieldLabel("Status Cucian"), gbc);

        cbPelanggan = createStyledComboBox();
        cbLayanan = createStyledComboBox();
        cbStatus = createStyledComboBox();
        cbStatus.setModel(new DefaultComboBoxModel<>(new String[]{"Antrean", "Diproses", "Selesai", "Diambil"}));

        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0.34; gbc.insets = new Insets(0, 0, 14, 14);
        inputGrid.add(cbPelanggan, gbc);

        gbc.gridx = 1; gbc.weightx = 0.38; gbc.insets = new Insets(0, 0, 14, 14);
        inputGrid.add(cbLayanan, gbc);

        gbc.gridx = 2; gbc.weightx = 0.28; gbc.insets = new Insets(0, 0, 14, 0);
        inputGrid.add(cbStatus, gbc);

        // Baris 2: Tanggal Masuk | Berat + Hitung | Total Harga
        gbc.gridy = 2;
        gbc.gridx = 0; gbc.weightx = 0.34; gbc.insets = new Insets(0, 0, 6, 14);
        inputGrid.add(createFieldLabel("Tanggal Masuk (YYYY-MM-DD)"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.38; gbc.insets = new Insets(0, 0, 6, 14);
        inputGrid.add(createFieldLabel("Berat Cucian (Kg)"), gbc);

        gbc.gridx = 2; gbc.weightx = 0.28; gbc.insets = new Insets(0, 0, 6, 0);
        inputGrid.add(createFieldLabel("Total Harga (Rp)"), gbc);

        txtTanggal = createStyledTextField("YYYY-MM-DD");
        txtTanggal.setText(LocalDate.now().toString());

        JPanel beratPanel = new JPanel(new BorderLayout(8, 0));
        beratPanel.setOpaque(false);
        txtBerat = createStyledTextField("Contoh: 2.5");
        btnHitung = createActionButton("Hitung", "#1E3A5F", "#172D4B", "#0F1E33", "#FFFFFF", false);
        btnHitung.setPreferredSize(new Dimension(88, 36));
        beratPanel.add(txtBerat, BorderLayout.CENTER);
        beratPanel.add(btnHitung, BorderLayout.EAST);

        txtTotal = createStyledTextField("Klik tombol Hitung");
        txtTotal.setEditable(false);
        txtTotal.putClientProperty("FlatLaf.style", "arc: 10; margin: 0,10,0,10; background: #F8FAFC; font: bold");

        gbc.gridy = 3;
        gbc.gridx = 0; gbc.weightx = 0.34; gbc.insets = new Insets(0, 0, 4, 14);
        inputGrid.add(txtTanggal, gbc);

        gbc.gridx = 1; gbc.weightx = 0.38; gbc.insets = new Insets(0, 0, 4, 14);
        inputGrid.add(beratPanel, gbc);

        gbc.gridx = 2; gbc.weightx = 0.28; gbc.insets = new Insets(0, 0, 4, 0);
        inputGrid.add(txtTotal, gbc);

        formCard.add(inputGrid, BorderLayout.CENTER);

        // Panel Tombol Aksi
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelTombol.setOpaque(false);
        panelTombol.setBorder(new EmptyBorder(8, 0, 0, 0));

        btnBatal = createActionButton("Batal / Reset", "#F1F5F9", "#E2E8F0", "#CBD5E1", "#334155", true);
        btnCetakNota = createActionButton("Cetak Nota", "#7C3AED", "#6D28D9", "#5B21B6", "#FFFFFF", false);
        btnHapus = createActionButton("Hapus", "#DC2626", "#B91C1C", "#991B1B", "#FFFFFF", false);
        btnUbah = createActionButton("Ubah", "#0F766E", "#115E59", "#134E4A", "#FFFFFF", false);
        btnSimpan = createActionButton("Simpan", "#0066CC", "#0052A3", "#003D7A", "#FFFFFF", false);

        panelTombol.add(btnBatal);
        panelTombol.add(btnCetakNota);
        panelTombol.add(btnHapus);
        panelTombol.add(btnUbah);
        panelTombol.add(btnSimpan);

        formCard.add(panelTombol, BorderLayout.SOUTH);
        add(formCard, BorderLayout.NORTH);

        // ==========================================
        // 2. KARTU TABEL TRANSAKSI + SEARCH BAR
        // ==========================================
        JPanel tableCard = new JPanel(new BorderLayout(0, 12));
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new EmptyBorder(18, 22, 18, 22));
        tableCard.putClientProperty("FlatLaf.style", "arc: 16");

        JPanel tableHeaderBar = new JPanel(new BorderLayout());
        tableHeaderBar.setOpaque(false);

        JLabel lblTableTitle = new JLabel("Riwayat & Daftar Semua Orderan");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTableTitle.setForeground(new Color(30, 41, 59));

        lblJumlahData = new JLabel("Total: 0 Orderan");
        lblJumlahData.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblJumlahData.setForeground(new Color(0, 102, 204));

        txtCari = new JTextField();
        txtCari.setPreferredSize(new Dimension(240, 34));
        txtCari.putClientProperty("JTextField.placeholderText", "Cari ID, pelanggan, layanan, status...");
        txtCari.putClientProperty("FlatLaf.style", "arc: 10; margin: 0,10,0,10; showClearButton: true");

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightHeader.setOpaque(false);
        rightHeader.add(lblJumlahData);
        rightHeader.add(txtCari);

        tableHeaderBar.add(lblTableTitle, BorderLayout.WEST);
        tableHeaderBar.add(rightHeader, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{"ID", "Pelanggan", "Layanan", "Tanggal", "Berat", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelTransaksi = new JTable(model);
        styleModernTable(tabelTransaksi);

        // Pasang Sorter untuk Search Bar Real-Time
        sorter = new TableRowSorter<>(model);
        tabelTransaksi.setRowSorter(sorter);

        txtCari.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String teks = txtCari.getText().trim();
                if (teks.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(teks)));
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }
        });

        JScrollPane scrollPane = new JScrollPane(tabelTransaksi);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableCard.add(tableHeaderBar, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        loadComboBox();
        loadData();
        resetForm();

        // ==========================================
        // 3. EVENT LISTENERS
        // ==========================================
        btnHitung.addActionListener(e -> hitungTotal());
        txtBerat.addActionListener(e -> hitungTotal());

        tabelTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int viewRow = tabelTransaksi.getSelectedRow();
                if (viewRow != -1) {
                    int row = tabelTransaksi.convertRowIndexToModel(viewRow);
                    idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());
                    pilihItemCombo(cbPelanggan, model.getValueAt(row, 1).toString());
                    pilihItemCombo(cbLayanan, model.getValueAt(row, 2).toString());
                    txtTanggal.setText(model.getValueAt(row, 3).toString());
                    txtBerat.setText(model.getValueAt(row, 4).toString());

                    String totalAsli = model.getValueAt(row, 5).toString();
                    if (totalAsli.endsWith(".0")) {
                        totalAsli = totalAsli.substring(0, totalAsli.length() - 2);
                    }
                    txtTotal.setText(totalAsli);

                    cbStatus.setSelectedItem(model.getValueAt(row, 6).toString());

                    btnSimpan.setEnabled(false);
                    btnUbah.setEnabled(true);
                    btnHapus.setEnabled(true);
                    btnCetakNota.setEnabled(true);
                }
            }
        });

        btnCetakNota.addActionListener(e -> {
            if (idTerpilih != 0) {
                ReportManager.cetakNota(idTerpilih);
            }
        });

        btnSimpan.addActionListener(e -> {
            if (validasiInput()) {
                Transaksi t = new Transaksi();
                t.setIdPelanggan(getIdFromCombo(cbPelanggan.getSelectedItem().toString()));
                t.setIdLayanan(getIdFromCombo(cbLayanan.getSelectedItem().toString()));
                t.setTanggalMasuk(txtTanggal.getText().trim());
                t.setBeratKg(Double.parseDouble(txtBerat.getText().trim()));
                t.setTotalHarga(Double.parseDouble(txtTotal.getText().trim()));
                t.setStatusCucian(cbStatus.getSelectedItem().toString());

                transaksiDAO.simpan(t);
                JOptionPane.showMessageDialog(this, "Orderan berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                resetForm();
            }
        });

        btnUbah.addActionListener(e -> {
            if (validasiInput() && idTerpilih != 0) {
                Transaksi t = new Transaksi();
                t.setIdTransaksi(idTerpilih);
                t.setIdPelanggan(getIdFromCombo(cbPelanggan.getSelectedItem().toString()));
                t.setIdLayanan(getIdFromCombo(cbLayanan.getSelectedItem().toString()));
                t.setTanggalMasuk(txtTanggal.getText().trim());
                t.setBeratKg(Double.parseDouble(txtBerat.getText().trim()));
                t.setTotalHarga(Double.parseDouble(txtTotal.getText().trim()));
                t.setStatusCucian(cbStatus.getSelectedItem().toString());

                transaksiDAO.ubah(t);
                JOptionPane.showMessageDialog(this, "Orderan berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                resetForm();
            }
        });

        btnHapus.addActionListener(e -> {
            if (idTerpilih != 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(
                        this,
                        "Yakin ingin menghapus orderan ID #" + idTerpilih + "?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    transaksiDAO.hapus(idTerpilih);
                    JOptionPane.showMessageDialog(this, "Orderan berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    resetForm();
                }
            }
        });

        btnBatal.addActionListener(e -> resetForm());
    }

    // --- HELPER KOMPONEN UI ---

    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(71, 85, 105));
        return lbl;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(160, 36));
        tf.putClientProperty("JTextField.placeholderText", placeholder);
        tf.putClientProperty("FlatLaf.style", "arc: 10; margin: 0,10,0,10");
        return tf;
    }

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setPreferredSize(new Dimension(160, 36));
        cb.putClientProperty("FlatLaf.style", "arc: 10");
        return cb;
    }

    private JButton createActionButton(String text, String bg, String hover, String pressed, String fg, boolean bordered) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(110, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String borderStyle = bordered ? "borderWidth: 1; borderColor: #CBD5E1; " : "borderWidth: 0; ";
        btn.putClientProperty("FlatLaf.style",
                "arc: 10; focusWidth: 0; " + borderStyle
                + "background: " + bg + "; hoverBackground: " + hover + "; pressedBackground: " + pressed + "; "
                + "foreground: " + fg + "; font: bold");
        return btn;
    }

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

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Object displayVal = value;
                if (column == 4 && value != null) {
                    displayVal = value + " Kg";
                } else if (column == 5 && value instanceof Number) {
                    displayVal = rupiahFormat.format(((Number) value).doubleValue());
                }

                Component c = super.getTableCellRendererComponent(tbl, displayVal, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(new Color(30, 41, 59));
                }

                if (column == 0 || column == 3 || column == 4 || column == 6) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 5) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                if (column == 6 && value != null && !isSelected) {
                    String status = value.toString();
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    if (status.equalsIgnoreCase("Antrean")) {
                        c.setForeground(new Color(217, 119, 6));
                    } else if (status.equalsIgnoreCase("Diproses")) {
                        c.setForeground(new Color(0, 102, 204));
                    } else if (status.equalsIgnoreCase("Selesai")) {
                        c.setForeground(new Color(15, 118, 110));
                    } else {
                        c.setForeground(new Color(100, 116, 139));
                    }
                } else {
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(55);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(145);
        table.getColumnModel().getColumn(3).setPreferredWidth(105);
        table.getColumnModel().getColumn(4).setPreferredWidth(75);
        table.getColumnModel().getColumn(5).setPreferredWidth(115);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
    }

    // --- LOGIKA DATA ---

    private void pilihItemCombo(JComboBox<String> combo, String kataKunci) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).contains(" - " + kataKunci)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    public void loadComboBox() {
        cbPelanggan.removeAllItems();
        cbLayanan.removeAllItems();

        for (Pelanggan p : pelangganDAO.ambilSemuaData()) {
            cbPelanggan.addItem(p.getIdPelanggan() + " - " + p.getNama());
        }

        for (Layanan l : layananDAO.ambilSemuaData()) {
            cbLayanan.addItem(l.getIdLayanan() + " - " + l.getNamaLayanan() + " - Rp" + l.getHargaPerKg());
        }
    }

    private void loadData() {
        model.setRowCount(0);
        List<Transaksi> list = transaksiDAO.ambilSemuaData();
        for (Transaksi t : list) {
            model.addRow(new Object[]{
                t.getIdTransaksi(), t.getNamaPelanggan(), t.getNamaLayanan(),
                t.getTanggalMasuk(), t.getBeratKg(), t.getTotalHarga(), t.getStatusCucian()
            });
        }
        lblJumlahData.setText("Total: " + list.size() + " Orderan");
    }

    private void hitungTotal() {
        try {
            double berat = Double.parseDouble(txtBerat.getText().trim());
            String layanan = cbLayanan.getSelectedItem().toString();
            String[] parts = layanan.split("- Rp");
            double harga = Double.parseDouble(parts[1]);
            double total = berat * harga;
            String totalStr = String.valueOf(total);
            if (totalStr.endsWith(".0")) {
                totalStr = totalStr.substring(0, totalStr.length() - 2);
            }
            txtTotal.setText(totalStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Pastikan berat sudah diisi dengan angka (gunakan titik untuk desimal)!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int getIdFromCombo(String comboItem) {
        return Integer.parseInt(comboItem.split(" - ")[0]);
    }

    private void resetForm() {
        txtTanggal.setText(LocalDate.now().toString());
        txtBerat.setText("");
        txtTotal.setText("");
        cbStatus.setSelectedIndex(0);
        idTerpilih = 0;
        tabelTransaksi.clearSelection();
        loadComboBox();
        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
        btnCetakNota.setEnabled(false);
    }

    private boolean validasiInput() {
        if (cbPelanggan.getSelectedItem() == null || cbLayanan.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pastikan data Pelanggan dan Layanan sudah tersedia!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtTotal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan klik tombol 'Hitung' terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
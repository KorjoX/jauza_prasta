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
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.dao.DataIntegrityViolationException;

public class FormPelanggan extends JPanel {

    private JTextField txtNama, txtNoHp, txtAlamat, txtCari;
    private JButton btnSimpan, btnUbah, btnHapus, btnBatal;
    private JTable tabelPelanggan;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel lblJumlahData;
    private int idTerpilih = 0;

    private final PelangganDAO pelangganDAO;

    public FormPelanggan(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;

        setLayout(new BorderLayout(0, 18));
        setBackground(new Color(244, 247, 251));
        setBorder(new EmptyBorder(22, 26, 22, 26));

        // ==========================================
        // 1. KARTU FORM INPUT (ATAS)
        // ==========================================
        JPanel formCard = new JPanel(new BorderLayout(0, 14));
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(new EmptyBorder(18, 22, 18, 22));
        formCard.putClientProperty("FlatLaf.style", "arc: 16");

        JLabel lblFormTitle = new JLabel("Input & Kelola Data Pelanggan");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormTitle.setForeground(new Color(30, 41, 59));
        formCard.add(lblFormTitle, BorderLayout.NORTH);

        JPanel inputGrid = new JPanel(new GridBagLayout());
        inputGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 8, 16);

        // Baris 1: Nama Pelanggan & No. Handphone
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        inputGrid.add(createFieldLabel("Nama Pelanggan"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5;
        gbc.insets = new Insets(4, 0, 8, 0);
        inputGrid.add(createFieldLabel("No. Handphone"), gbc);

        txtNama = createStyledTextField("Contoh: Budi Santoso");
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 0, 12, 16);
        inputGrid.add(txtNama, gbc);

        txtNoHp = createStyledTextField("Contoh: 081234567890");
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.5;
        gbc.insets = new Insets(0, 0, 12, 0);
        inputGrid.add(txtNoHp, gbc);

        // Baris 2: Alamat (Lebar Penuh)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 6, 0);
        inputGrid.add(createFieldLabel("Alamat Lengkap"), gbc);

        txtAlamat = createStyledTextField("Masukkan alamat lengkap pelanggan");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 4, 0);
        inputGrid.add(txtAlamat, gbc);

        formCard.add(inputGrid, BorderLayout.CENTER);

        // Panel Tombol Aksi
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelTombol.setOpaque(false);
        panelTombol.setBorder(new EmptyBorder(6, 0, 0, 0));

        btnBatal = createActionButton("Batal / Reset", "#F1F5F9", "#E2E8F0", "#CBD5E1", "#334155", true);
        btnHapus = createActionButton("Hapus", "#DC2626", "#B91C1C", "#991B1B", "#FFFFFF", false);
        btnUbah = createActionButton("Ubah", "#0F766E", "#115E59", "#134E4A", "#FFFFFF", false);
        btnSimpan = createActionButton("Simpan", "#0066CC", "#0052A3", "#003D7A", "#FFFFFF", false);

        panelTombol.add(btnBatal);
        panelTombol.add(btnHapus);
        panelTombol.add(btnUbah);
        panelTombol.add(btnSimpan);

        formCard.add(panelTombol, BorderLayout.SOUTH);
        add(formCard, BorderLayout.NORTH);

        // ==========================================
        // 2. KARTU TABEL PELANGGAN + SEARCH BAR
        // ==========================================
        JPanel tableCard = new JPanel(new BorderLayout(0, 12));
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new EmptyBorder(18, 22, 18, 22));
        tableCard.putClientProperty("FlatLaf.style", "arc: 16");

        JPanel tableHeaderBar = new JPanel(new BorderLayout());
        tableHeaderBar.setOpaque(false);

        JLabel lblTableTitle = new JLabel("Daftar Pelanggan Terdaftar");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTableTitle.setForeground(new Color(30, 41, 59));

        lblJumlahData = new JLabel("Total: 0 Pelanggan");
        lblJumlahData.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblJumlahData.setForeground(new Color(0, 102, 204));

        txtCari = new JTextField();
        txtCari.setPreferredSize(new Dimension(230, 34));
        txtCari.putClientProperty("JTextField.placeholderText", "Cari nama, no HP, atau alamat...");
        txtCari.putClientProperty("FlatLaf.style", "arc: 10; margin: 0,10,0,10; showClearButton: true");

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightHeader.setOpaque(false);
        rightHeader.add(lblJumlahData);
        rightHeader.add(txtCari);

        tableHeaderBar.add(lblTableTitle, BorderLayout.WEST);
        tableHeaderBar.add(rightHeader, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{"ID", "Nama Pelanggan", "No. Handphone", "Alamat"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelPelanggan = new JTable(model);
        styleModernTable(tabelPelanggan);

        // Pasang Sorter untuk Search Bar Real-Time
        sorter = new TableRowSorter<>(model);
        tabelPelanggan.setRowSorter(sorter);

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

        JScrollPane scrollPane = new JScrollPane(tabelPelanggan);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableCard.add(tableHeaderBar, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        loadData();
        resetForm();

        // ==========================================
        // 3. EVENT LISTENERS
        // ==========================================
        tabelPelanggan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int viewRow = tabelPelanggan.getSelectedRow();
                if (viewRow != -1) {
                    int row = tabelPelanggan.convertRowIndexToModel(viewRow);
                    idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());
                    txtNama.setText(model.getValueAt(row, 1).toString());
                    txtNoHp.setText(model.getValueAt(row, 2).toString());
                    Object alamatVal = model.getValueAt(row, 3);
                    txtAlamat.setText(alamatVal != null ? alamatVal.toString() : "");

                    btnSimpan.setEnabled(false);
                    btnUbah.setEnabled(true);
                    btnHapus.setEnabled(true);
                }
            }
        });

        btnSimpan.addActionListener(e -> {
            if (validasiInput()) {
                Pelanggan p = new Pelanggan();
                p.setNama(txtNama.getText().trim());
                p.setNoHp(txtNoHp.getText().trim());
                p.setAlamat(txtAlamat.getText().trim());

                pelangganDAO.simpan(p);

                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                resetForm();
            }
        });

        btnUbah.addActionListener(e -> {
            if (validasiInput() && idTerpilih != 0) {
                Pelanggan p = new Pelanggan();
                p.setIdPelanggan(idTerpilih);
                p.setNama(txtNama.getText().trim());
                p.setNoHp(txtNoHp.getText().trim());
                p.setAlamat(txtAlamat.getText().trim());

                pelangganDAO.ubah(p);

                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                resetForm();
            }
        });

        btnHapus.addActionListener(e -> {
            if (idTerpilih != 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(
                        this,
                        "Yakin ingin menghapus pelanggan \"" + txtNama.getText() + "\"?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    try {
                        pelangganDAO.hapus(idTerpilih);
                        JOptionPane.showMessageDialog(this, "Data pelanggan berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        resetForm();
                    } catch (DataIntegrityViolationException ex) {
                        JOptionPane.showMessageDialog(this, "Gagal! Pelanggan ini masih memiliki riwayat transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
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
        tf.setPreferredSize(new Dimension(200, 36));
        tf.putClientProperty("JTextField.placeholderText", placeholder);
        tf.putClientProperty("FlatLaf.style", "arc: 10; margin: 0,10,0,10");
        return tf;
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
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(new Color(30, 41, 59));
                }
                setHorizontalAlignment(column == 0 || column == 2 ? SwingConstants.CENTER : SwingConstants.LEFT);
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(320);
    }

    // --- LOGIKA DATA ---

    private void loadData() {
        model.setRowCount(0);
        List<Pelanggan> list = pelangganDAO.ambilSemuaData();
        for (Pelanggan p : list) {
            model.addRow(new Object[]{p.getIdPelanggan(), p.getNama(), p.getNoHp(), p.getAlamat()});
        }
        lblJumlahData.setText("Total: " + list.size() + " Pelanggan");
    }

    private void resetForm() {
        txtNama.setText("");
        txtNoHp.setText("");
        txtAlamat.setText("");
        idTerpilih = 0;
        tabelPelanggan.clearSelection();
        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    private boolean validasiInput() {
        if (txtNama.getText().trim().isEmpty() || txtNoHp.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan No. HP wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
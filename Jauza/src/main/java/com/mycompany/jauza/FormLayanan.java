package com.mycompany.jauza;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;

public class FormLayanan extends JPanel {

    private JTextField txtNamaLayanan, txtHarga;
    private JButton btnSimpan, btnUbah, btnHapus, btnBatal;
    private JTable tabelLayanan;
    private DefaultTableModel model;
    private JLabel lblJumlahData;
    private int idTerpilih = 0;

    private final LayananDAO layananDAO;
    private final DecimalFormat rupiahFormat = new DecimalFormat("Rp #,##0");

    public FormLayanan(LayananDAO layananDAO) {
        this.layananDAO = layananDAO;

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

        JLabel lblFormTitle = new JLabel("Input & Kelola Jenis Layanan Cucian");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormTitle.setForeground(new Color(30, 41, 59));
        formCard.add(lblFormTitle, BorderLayout.NORTH);

        // Grid 2 kolom sejajar
        JPanel inputGrid = new JPanel(new GridBagLayout());
        inputGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.6;
        gbc.insets = new Insets(4, 0, 6, 16);
        inputGrid.add(createFieldLabel("Nama Layanan"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        gbc.insets = new Insets(4, 0, 6, 0);
        inputGrid.add(createFieldLabel("Harga per Kg (Rp)"), gbc);

        txtNamaLayanan = createStyledTextField("Contoh: Cuci Komplit Express");
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, 4, 16);
        inputGrid.add(txtNamaLayanan, gbc);

        txtHarga = createStyledTextField("Contoh: 8000");
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        gbc.insets = new Insets(0, 0, 4, 0);
        inputGrid.add(txtHarga, gbc);

        formCard.add(inputGrid, BorderLayout.CENTER);

        // Panel Tombol Aksi
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelTombol.setOpaque(false);
        panelTombol.setBorder(new EmptyBorder(8, 0, 0, 0));

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
        // 2. KARTU TABEL LAYANAN (TENGAH)
        // ==========================================
        JPanel tableCard = new JPanel(new BorderLayout(0, 12));
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new EmptyBorder(18, 22, 18, 22));
        tableCard.putClientProperty("FlatLaf.style", "arc: 16");

        JPanel tableHeaderBar = new JPanel(new BorderLayout());
        tableHeaderBar.setOpaque(false);

        JLabel lblTableTitle = new JLabel("Daftar Layanan Tersedia");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTableTitle.setForeground(new Color(30, 41, 59));

        lblJumlahData = new JLabel("Total: 0 Layanan");
        lblJumlahData.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblJumlahData.setForeground(new Color(0, 102, 204));

        tableHeaderBar.add(lblTableTitle, BorderLayout.WEST);
        tableHeaderBar.add(lblJumlahData, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{"ID Layanan", "Nama Layanan", "Harga per Kg"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelLayanan = new JTable(model);
        styleModernTable(tabelLayanan);

        JScrollPane scrollPane = new JScrollPane(tabelLayanan);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tableCard.add(tableHeaderBar, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);

        loadData();
        resetForm();

        // ==========================================
        // 3. EVENT LISTENERS (LOGIKA DAO UTUH)
        // ==========================================
        tabelLayanan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tabelLayanan.getSelectedRow();
                if (row != -1) {
                    idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());
                    txtNamaLayanan.setText(model.getValueAt(row, 1).toString());

                    String hargaAsli = model.getValueAt(row, 2).toString();
                    if (hargaAsli.endsWith(".0")) {
                        hargaAsli = hargaAsli.substring(0, hargaAsli.length() - 2);
                    }
                    txtHarga.setText(hargaAsli);

                    btnSimpan.setEnabled(false);
                    btnUbah.setEnabled(true);
                    btnHapus.setEnabled(true);
                }
            }
        });

        btnSimpan.addActionListener(e -> {
            if (validasiInput()) {
                Layanan l = new Layanan();
                l.setNamaLayanan(txtNamaLayanan.getText().trim());
                l.setHargaPerKg(Double.parseDouble(txtHarga.getText().trim()));
                layananDAO.simpan(l);
                JOptionPane.showMessageDialog(this, "Data layanan berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                resetForm();
            }
        });

        btnUbah.addActionListener(e -> {
            if (validasiInput() && idTerpilih != 0) {
                Layanan l = new Layanan();
                l.setIdLayanan(idTerpilih);
                l.setNamaLayanan(txtNamaLayanan.getText().trim());
                l.setHargaPerKg(Double.parseDouble(txtHarga.getText().trim()));
                layananDAO.ubah(l);
                JOptionPane.showMessageDialog(this, "Data layanan berhasil diubah!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                resetForm();
            }
        });

        btnHapus.addActionListener(e -> {
            if (idTerpilih != 0) {
                int kon = JOptionPane.showConfirmDialog(
                        this,
                        "Yakin ingin menghapus layanan \"" + txtNamaLayanan.getText() + "\"?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (kon == JOptionPane.YES_OPTION) {
                    try {
                        layananDAO.hapus(idTerpilih);
                        JOptionPane.showMessageDialog(this, "Data layanan berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        resetForm();
                    } catch (DataIntegrityViolationException ex) {
                        JOptionPane.showMessageDialog(this, "Gagal! Layanan ini masih dipakai di data transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
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
                Object displayVal = value;
                if (column == 2 && value instanceof Number) {
                    displayVal = rupiahFormat.format(((Number) value).doubleValue());
                }
                Component c = super.getTableCellRendererComponent(tbl, displayVal, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                    c.setForeground(new Color(30, 41, 59));
                }
                if (column == 0) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 2) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(380);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
    }

    // --- LOGIKA DATA ---

    private void loadData() {
        model.setRowCount(0);
        List<Layanan> list = layananDAO.ambilSemuaData();
        for (Layanan l : list) {
            model.addRow(new Object[]{l.getIdLayanan(), l.getNamaLayanan(), l.getHargaPerKg()});
        }
        lblJumlahData.setText("Total: " + list.size() + " Layanan");
    }

    private void resetForm() {
        txtNamaLayanan.setText("");
        txtHarga.setText("");
        idTerpilih = 0;
        tabelLayanan.clearSelection();
        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    private boolean validasiInput() {
        if (txtNamaLayanan.getText().trim().isEmpty() || txtHarga.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama layanan dan harga wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(txtHarga.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga per Kg harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
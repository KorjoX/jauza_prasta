package com.mycompany.jauza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FormTransaksi extends JPanel {

    private JComboBox<String> cbPelanggan, cbLayanan, cbStatus;
    private JTextField txtTanggal, txtBerat, txtTotal;
    private JButton btnHitung, btnSimpan, btnUbah, btnHapus, btnBatal;
    private JTable tabelTransaksi;
    private DefaultTableModel model;
    private int idTerpilih = 0;

    // Form ini sekarang menggunakan 3 DAO sekaligus, TIDAK ADA LAGI kata "koneksi"
    private TransaksiDAO transaksiDAO;
    private PelangganDAO pelangganDAO;
    private LayananDAO layananDAO;

    // Konstruktor menerima 3 DAO dari MenuUtama
    public FormTransaksi(TransaksiDAO transaksiDAO, PelangganDAO pelangganDAO, LayananDAO layananDAO) {
        this.transaksiDAO = transaksiDAO;
        this.pelangganDAO = pelangganDAO;
        this.layananDAO = layananDAO;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PANEL INPUT ---
        JPanel panelInput = new JPanel(new GridLayout(6, 3, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input Orderan Cucian"));

        panelInput.add(new JLabel("Pilih Pelanggan:"));
        cbPelanggan = new JComboBox<>();
        panelInput.add(cbPelanggan);
        panelInput.add(new JLabel(""));

        panelInput.add(new JLabel("Pilih Layanan:"));
        cbLayanan = new JComboBox<>();
        panelInput.add(cbLayanan);
        panelInput.add(new JLabel(""));

        panelInput.add(new JLabel("Tanggal Masuk (YYYY-MM-DD):"));
        txtTanggal = new JTextField(LocalDate.now().toString());
        panelInput.add(txtTanggal);
        panelInput.add(new JLabel(""));

        panelInput.add(new JLabel("Berat Cucian (Kg):"));
        txtBerat = new JTextField();
        panelInput.add(txtBerat);
        btnHitung = new JButton("Hitung Total");
        panelInput.add(btnHitung);

        panelInput.add(new JLabel("Total Harga (Rp):"));
        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        panelInput.add(txtTotal);
        panelInput.add(new JLabel(""));

        panelInput.add(new JLabel("Status Cucian:"));
        cbStatus = new JComboBox<>(new String[]{"Antrean", "Diproses", "Selesai", "Diambil"});
        panelInput.add(cbStatus);
        panelInput.add(new JLabel(""));

        // --- PANEL TOMBOL ---
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSimpan = new JButton("Simpan");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnBatal = new JButton("Batal");

        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnBatal);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelInput, BorderLayout.CENTER);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);
        add(panelAtas, BorderLayout.NORTH);

        // --- PANEL TABEL ---
        model = new DefaultTableModel(new String[]{"ID", "Pelanggan", "Layanan", "Tanggal", "Berat", "Total", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelTransaksi = new JTable(model);
        add(new JScrollPane(tabelTransaksi), BorderLayout.CENTER);

        loadComboBox();
        loadData();
        resetForm();

        // --- EVENT LISTENERS ---
        btnHitung.addActionListener(e -> hitungTotal());

        tabelTransaksi.getSelectionModel().addListSelectionListener(e -> {
            int row = tabelTransaksi.getSelectedRow();
            if (row != -1) {
                idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());
                txtTanggal.setText(model.getValueAt(row, 3).toString());
                txtBerat.setText(model.getValueAt(row, 4).toString());
                
                String totalAsli = model.getValueAt(row, 5).toString();
                if(totalAsli.endsWith(".0")) totalAsli = totalAsli.substring(0, totalAsli.length()-2);
                txtTotal.setText(totalAsli);
                
                cbStatus.setSelectedItem(model.getValueAt(row, 6).toString());
                
                btnSimpan.setEnabled(false);
                btnUbah.setEnabled(true);
                btnHapus.setEnabled(true);
            }
        });

        btnSimpan.addActionListener(e -> {
            if (validasiInput()) {
                Transaksi t = new Transaksi();
                t.setIdPelanggan(getIdFromCombo(cbPelanggan.getSelectedItem().toString()));
                t.setIdLayanan(getIdFromCombo(cbLayanan.getSelectedItem().toString()));
                t.setTanggalMasuk(txtTanggal.getText());
                t.setBeratKg(Double.parseDouble(txtBerat.getText()));
                t.setTotalHarga(Double.parseDouble(txtTotal.getText()));
                t.setStatusCucian(cbStatus.getSelectedItem().toString());
                
                transaksiDAO.simpan(t);
                JOptionPane.showMessageDialog(this, "Orderan berhasil disimpan!");
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
                t.setTanggalMasuk(txtTanggal.getText());
                t.setBeratKg(Double.parseDouble(txtBerat.getText()));
                t.setTotalHarga(Double.parseDouble(txtTotal.getText()));
                t.setStatusCucian(cbStatus.getSelectedItem().toString());
                
                transaksiDAO.ubah(t);
                JOptionPane.showMessageDialog(this, "Orderan berhasil diubah!");
                loadData();
                resetForm();
            }
        });

        btnHapus.addActionListener(e -> {
            if (idTerpilih != 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin hapus orderan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    transaksiDAO.hapus(idTerpilih);
                    JOptionPane.showMessageDialog(this, "Orderan dihapus!");
                    loadData();
                    resetForm();
                }
            }
        });

        btnBatal.addActionListener(e -> resetForm());
    }

    private void loadComboBox() {
        cbPelanggan.removeAllItems();
        cbLayanan.removeAllItems();
        
        // Memuat data dari DAO Pelanggan
        for (Pelanggan p : pelangganDAO.ambilSemuaData()) {
            cbPelanggan.addItem(p.getIdPelanggan() + " - " + p.getNama());
        }
        
        // Memuat data dari DAO Layanan
        for (Layanan l : layananDAO.ambilSemuaData()) {
            cbLayanan.addItem(l.getIdLayanan() + " - " + l.getNamaLayanan() + " - Rp" + l.getHargaPerKg());
        }
    }

    private void loadData() {
        model.setRowCount(0);
        for (Transaksi t : transaksiDAO.ambilSemuaData()) {
            model.addRow(new Object[]{
                t.getIdTransaksi(), t.getNamaPelanggan(), t.getNamaLayanan(),
                t.getTanggalMasuk(), t.getBeratKg(), t.getTotalHarga(), t.getStatusCucian()
            });
        }
    }

    private void hitungTotal() {
        try {
            double berat = Double.parseDouble(txtBerat.getText());
            String layanan = cbLayanan.getSelectedItem().toString();
            String[] parts = layanan.split("- Rp");
            double harga = Double.parseDouble(parts[1]);
            txtTotal.setText(String.valueOf(berat * harga));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Pastikan berat sudah diisi dengan angka (gunakan titik untuk desimal)!");
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
    }

    private boolean validasiInput() {
        if (txtTotal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan klik tombol 'Hitung Total' terlebih dahulu!");
            return false;
        }
        return true;
    }
}
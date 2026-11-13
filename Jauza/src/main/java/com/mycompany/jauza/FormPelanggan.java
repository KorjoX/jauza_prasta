package com.mycompany.jauza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormPelanggan extends JPanel {

    private JTextField txtNama, txtNoHp, txtAlamat;
    private JButton btnSimpan, btnUbah, btnHapus, btnBatal;
    private JTable tabelPelanggan;
    private DefaultTableModel model;
    private int idTerpilih = 0;
    
    // 1. Memanggil DAO yang akan dikerjakan oleh Spring
    private PelangganDAO pelangganDAO;

    // 2. Meminta PelangganDAO melalui Konstruktor agar disuntik otomatis
    public FormPelanggan(PelangganDAO pelangganDAO) {
        this.pelangganDAO = pelangganDAO;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PANEL INPUT FORM (Atas) ---
        JPanel panelInput = new JPanel(new GridLayout(4, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input Data Pelanggan"));

        panelInput.add(new JLabel("Nama Pelanggan:"));
        txtNama = new JTextField();
        panelInput.add(txtNama);

        panelInput.add(new JLabel("No. Handphone:"));
        txtNoHp = new JTextField();
        panelInput.add(txtNoHp);

        panelInput.add(new JLabel("Alamat:"));
        txtAlamat = new JTextField();
        panelInput.add(txtAlamat);

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
        model = new DefaultTableModel(new String[]{"ID", "Nama", "No HP", "Alamat"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelPelanggan = new JTable(model);
        add(new JScrollPane(tabelPelanggan), BorderLayout.CENTER);

        loadData();
        resetForm();

        // --- EVENT LISTENERS ---
        tabelPelanggan.getSelectionModel().addListSelectionListener(e -> {
            int row = tabelPelanggan.getSelectedRow();
            if (row != -1) {
                idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());
                txtNama.setText(model.getValueAt(row, 1).toString());
                txtNoHp.setText(model.getValueAt(row, 2).toString());
                txtAlamat.setText(model.getValueAt(row, 3).toString());
                
                btnSimpan.setEnabled(false);
                btnUbah.setEnabled(true);
                btnHapus.setEnabled(true);
            }
        });

        // LIHAT BETAPA BERSIHNYA KODE SIMPAN SEKARANG! Tanpa try-catch dan SQL panjang
        btnSimpan.addActionListener(e -> {
            if (validasiInput()) {
                Pelanggan p = new Pelanggan();
                p.setNama(txtNama.getText());
                p.setNoHp(txtNoHp.getText());
                p.setAlamat(txtAlamat.getText());
                
                pelangganDAO.simpan(p); 
                
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                loadData();
                resetForm();
            }
        });

        btnUbah.addActionListener(e -> {
            if (validasiInput() && idTerpilih != 0) {
                Pelanggan p = new Pelanggan();
                p.setIdPelanggan(idTerpilih);
                p.setNama(txtNama.getText());
                p.setNoHp(txtNoHp.getText());
                p.setAlamat(txtAlamat.getText());
                
                pelangganDAO.ubah(p);
                
                JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
                loadData();
                resetForm();
            }
        });

        btnHapus.addActionListener(e -> {
            if (idTerpilih != 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    pelangganDAO.hapus(idTerpilih);
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    loadData();
                    resetForm();
                }
            }
        });

        btnBatal.addActionListener(e -> resetForm());
    }

    private void loadData() {
        model.setRowCount(0);
        // Mengambil list pelanggan dari DAO
        List<Pelanggan> list = pelangganDAO.ambilSemuaData();
        for (Pelanggan p : list) {
            model.addRow(new Object[]{p.getIdPelanggan(), p.getNama(), p.getNoHp(), p.getAlamat()});
        }
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
            JOptionPane.showMessageDialog(this, "Nama dan No. HP wajib diisi!");
            return false;
        }
        return true;
    }
}
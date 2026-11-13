package com.mycompany.jauza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;

public class FormLayanan extends JPanel {

    private JTextField txtNamaLayanan, txtHarga;
    private JButton btnSimpan, btnUbah, btnHapus, btnBatal;
    private JTable tabelLayanan;
    private DefaultTableModel model;
    private int idTerpilih = 0;
    
    private LayananDAO layananDAO;

    public FormLayanan(LayananDAO layananDAO) {
        this.layananDAO = layananDAO;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelInput = new JPanel(new GridLayout(3, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Input Data Layanan Cucian"));

        panelInput.add(new JLabel("Nama Layanan:"));
        txtNamaLayanan = new JTextField();
        panelInput.add(txtNamaLayanan);

        panelInput.add(new JLabel("Harga per Kg (Rp):"));
        txtHarga = new JTextField();
        panelInput.add(txtHarga);

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

        model = new DefaultTableModel(new String[]{"ID Layanan", "Nama Layanan", "Harga per Kg (Rp)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelLayanan = new JTable(model);
        add(new JScrollPane(tabelLayanan), BorderLayout.CENTER);

        loadData();
        resetForm();

        tabelLayanan.getSelectionModel().addListSelectionListener(e -> {
            int row = tabelLayanan.getSelectedRow();
            if (row != -1) {
                idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());
                txtNamaLayanan.setText(model.getValueAt(row, 1).toString());
                
                String hargaAsli = model.getValueAt(row, 2).toString();
                if(hargaAsli.endsWith(".0")) hargaAsli = hargaAsli.substring(0, hargaAsli.length()-2);
                txtHarga.setText(hargaAsli);
                
                btnSimpan.setEnabled(false);
                btnUbah.setEnabled(true);
                btnHapus.setEnabled(true);
            }
        });

        btnSimpan.addActionListener(e -> {
            if (validasiInput()) {
                Layanan l = new Layanan();
                l.setNamaLayanan(txtNamaLayanan.getText());
                l.setHargaPerKg(Double.parseDouble(txtHarga.getText()));
                layananDAO.simpan(l);
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                loadData();
                resetForm();
            }
        });

        btnUbah.addActionListener(e -> {
            if (validasiInput() && idTerpilih != 0) {
                Layanan l = new Layanan();
                l.setIdLayanan(idTerpilih);
                l.setNamaLayanan(txtNamaLayanan.getText());
                l.setHargaPerKg(Double.parseDouble(txtHarga.getText()));
                layananDAO.ubah(l);
                JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
                loadData();
                resetForm();
            }
        });

        btnHapus.addActionListener(e -> {
            if (idTerpilih != 0) {
                int kon = JOptionPane.showConfirmDialog(this, "Yakin hapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (kon == JOptionPane.YES_OPTION) {
                    try {
                        layananDAO.hapus(idTerpilih);
                        JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                        loadData();
                        resetForm();
                    } catch (DataIntegrityViolationException ex) {
                        JOptionPane.showMessageDialog(this, "Gagal! Data masih dipakai di transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnBatal.addActionListener(e -> resetForm());
    }

    private void loadData() {
        model.setRowCount(0);
        for (Layanan l : layananDAO.ambilSemuaData()) {
            model.addRow(new Object[]{l.getIdLayanan(), l.getNamaLayanan(), l.getHargaPerKg()});
        }
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
            JOptionPane.showMessageDialog(this, "Wajib diisi!");
            return false;
        }
        return true;
    }
}
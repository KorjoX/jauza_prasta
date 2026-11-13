package com.mycompany.jauza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class TransaksiDAOImpl implements TransaksiDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void simpan(Transaksi t) {
        String sql = "INSERT INTO transaksi (id_pelanggan, id_layanan, tanggal_masuk, berat_kg, total_harga, status_cucian) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, t.getIdPelanggan(), t.getIdLayanan(), t.getTanggalMasuk(), t.getBeratKg(), t.getTotalHarga(), t.getStatusCucian());
    }

    @Override
    public void ubah(Transaksi t) {
        String sql = "UPDATE transaksi SET id_pelanggan=?, id_layanan=?, tanggal_masuk=?, berat_kg=?, total_harga=?, status_cucian=? WHERE id_transaksi=?";
        jdbcTemplate.update(sql, t.getIdPelanggan(), t.getIdLayanan(), t.getTanggalMasuk(), t.getBeratKg(), t.getTotalHarga(), t.getStatusCucian(), t.getIdTransaksi());
    }

    @Override
    public void hapus(int idTransaksi) {
        String sql = "DELETE FROM transaksi WHERE id_transaksi=?";
        jdbcTemplate.update(sql, idTransaksi);
    }

    @Override
    public List<Transaksi> ambilSemuaData() {
        String sql = "SELECT t.*, p.nama, l.nama_layanan FROM transaksi t " +
                     "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan " +
                     "JOIN layanan l ON t.id_layanan = l.id_layanan ORDER BY t.id_transaksi DESC";
                     
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Transaksi t = new Transaksi();
            t.setIdTransaksi(rs.getInt("id_transaksi"));
            t.setIdPelanggan(rs.getInt("id_pelanggan"));
            t.setIdLayanan(rs.getInt("id_layanan"));
            t.setTanggalMasuk(rs.getString("tanggal_masuk"));
            t.setBeratKg(rs.getDouble("berat_kg"));
            t.setTotalHarga(rs.getDouble("total_harga"));
            t.setStatusCucian(rs.getString("status_cucian"));
            
            t.setNamaPelanggan(rs.getString("nama"));
            t.setNamaLayanan(rs.getString("nama_layanan"));
            return t;
        });
    }
}
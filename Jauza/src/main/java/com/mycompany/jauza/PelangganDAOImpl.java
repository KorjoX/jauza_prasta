package com.mycompany.jauza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

// Anotasi @Repository memberitahu Spring bahwa kelas ini bertugas mengelola database
@Repository
public class PelangganDAOImpl implements PelangganDAO {

    // Spring akan otomatis menyuntikkan JdbcTemplate dari AppConfig ke sini
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void simpan(Pelanggan p) {
        String sql = "INSERT INTO pelanggan (nama, no_hp, alamat) VALUES (?, ?, ?)";
        // Tidak perlu lagi try-catch dan buka-tutup koneksi secara manual!
        jdbcTemplate.update(sql, p.getNama(), p.getNoHp(), p.getAlamat());
    }

    @Override
    public void ubah(Pelanggan p) {
        String sql = "UPDATE pelanggan SET nama=?, no_hp=?, alamat=? WHERE id_pelanggan=?";
        jdbcTemplate.update(sql, p.getNama(), p.getNoHp(), p.getAlamat(), p.getIdPelanggan());
    }

    @Override
    public void hapus(int idPelanggan) {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan=?";
        jdbcTemplate.update(sql, idPelanggan);
    }

    @Override
    public List<Pelanggan> ambilSemuaData() {
        String sql = "SELECT * FROM pelanggan";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Pelanggan p = new Pelanggan();
            p.setIdPelanggan(rs.getInt("id_pelanggan"));
            p.setNama(rs.getString("nama"));
            p.setNoHp(rs.getString("no_hp"));
            p.setAlamat(rs.getString("alamat"));
            return p;
        });
    }
}
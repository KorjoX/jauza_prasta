package com.mycompany.jauza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

// Anotasi ini SANGAT PENTING agar Spring mengenali file ini sebagai mesin database
@Repository
public class LayananDAOImpl implements LayananDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void simpan(Layanan l) {
        String sql = "INSERT INTO layanan (nama_layanan, harga_per_kg) VALUES (?, ?)";
        jdbcTemplate.update(sql, l.getNamaLayanan(), l.getHargaPerKg());
    }

    @Override
    public void ubah(Layanan l) {
        String sql = "UPDATE layanan SET nama_layanan=?, harga_per_kg=? WHERE id_layanan=?";
        jdbcTemplate.update(sql, l.getNamaLayanan(), l.getHargaPerKg(), l.getIdLayanan());
    }

    @Override
    public void hapus(int idLayanan) {
        String sql = "DELETE FROM layanan WHERE id_layanan=?";
        jdbcTemplate.update(sql, idLayanan);
    }

    @Override
    public List<Layanan> ambilSemuaData() {
        String sql = "SELECT * FROM layanan";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Layanan l = new Layanan();
            l.setIdLayanan(rs.getInt("id_layanan"));
            l.setNamaLayanan(rs.getString("nama_layanan"));
            l.setHargaPerKg(rs.getDouble("harga_per_kg"));
            return l;
        });
    }
}
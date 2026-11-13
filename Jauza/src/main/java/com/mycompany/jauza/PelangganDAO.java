package com.mycompany.jauza;

import java.util.List;

public interface PelangganDAO {
    void simpan(Pelanggan pelanggan);
    void ubah(Pelanggan pelanggan);
    void hapus(int idPelanggan);
    List<Pelanggan> ambilSemuaData();
}
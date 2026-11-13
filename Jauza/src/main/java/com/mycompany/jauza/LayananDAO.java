package com.mycompany.jauza;

import java.util.List;

public interface LayananDAO {
    void simpan(Layanan layanan);
    void ubah(Layanan layanan);
    void hapus(int idLayanan);
    List<Layanan> ambilSemuaData();
}
package com.mycompany.jauza;

import java.util.List;

public interface TransaksiDAO {
    void simpan(Transaksi transaksi);
    void ubah(Transaksi transaksi);
    void hapus(int idTransaksi);
    List<Transaksi> ambilSemuaData();
}
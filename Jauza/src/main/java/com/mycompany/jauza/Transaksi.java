package com.mycompany.jauza;

public class Transaksi {
    private int idTransaksi;
    private int idPelanggan;
    private int idLayanan;
    private String tanggalMasuk;
    private double beratKg;
    private double totalHarga;
    private String statusCucian;
    
    // Variabel tambahan untuk menampung hasil JOIN tabel
    private String namaPelanggan;
    private String namaLayanan;

    // Getter dan Setter
    public int getIdTransaksi() { return idTransaksi; }
    public void setIdTransaksi(int idTransaksi) { this.idTransaksi = idTransaksi; }

    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }

    public int getIdLayanan() { return idLayanan; }
    public void setIdLayanan(int idLayanan) { this.idLayanan = idLayanan; }

    public String getTanggalMasuk() { return tanggalMasuk; }
    public void setTanggalMasuk(String tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }

    public double getBeratKg() { return beratKg; }
    public void setBeratKg(double beratKg) { this.beratKg = beratKg; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }

    public String getStatusCucian() { return statusCucian; }
    public void setStatusCucian(String statusCucian) { this.statusCucian = statusCucian; }

    public String getNamaPelanggan() { return namaPelanggan; }
    public void setNamaPelanggan(String namaPelanggan) { this.namaPelanggan = namaPelanggan; }

    public String getNamaLayanan() { return namaLayanan; }
    public void setNamaLayanan(String namaLayanan) { this.namaLayanan = namaLayanan; }
}
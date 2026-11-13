package com.mycompany.jauza;

public class Pelanggan {
    private int idPelanggan;
    private String nama;
    private String noHp;
    private String alamat;

    // Getter dan Setter untuk mengambil dan mengisi data
    public int getIdPelanggan() { return idPelanggan; }
    public void setIdPelanggan(int idPelanggan) { this.idPelanggan = idPelanggan; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
}
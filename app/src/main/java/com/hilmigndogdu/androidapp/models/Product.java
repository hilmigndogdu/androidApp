package com.hilmigndogdu.androidapp.models;

public class Product {
    private int id;                // ID alanı eklendi
    private String urunAdi;
    private double fiyat;
    private int yorumSayisi;
    private double puan;
    private int resimKaynak;
    private String kategori;
    private int adet;

    public Product(int id, String urunAdi, double fiyat, int yorumSayisi, double puan, int resimKaynak) {
        this.id = id;
        this.urunAdi = urunAdi;
        this.fiyat = fiyat;
        this.yorumSayisi = yorumSayisi;
        this.puan = puan;
        this.resimKaynak = resimKaynak;
        this.adet = 1;
    }

    // Getter ve Setter metotları
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }

    public double getFiyat() {
        return fiyat;
    }

    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }

    public int getYorumSayisi() {
        return yorumSayisi;
    }

    public void setYorumSayisi(int yorumSayisi) {
        this.yorumSayisi = yorumSayisi;
    }

    public double getPuan() {
        return puan;
    }

    public void setPuan(double puan) {
        this.puan = puan;
    }

    public int getResimKaynak() {
        return resimKaynak;
    }

    public void setResimKaynak(int resimKaynak) {
        this.resimKaynak = resimKaynak;
    }

    public String getCategory() {
        return kategori;
    }

    public void setCategory(String kategori) {
        this.kategori = kategori;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }
}

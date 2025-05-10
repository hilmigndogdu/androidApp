package com.hilmigndogdu.androidapp;

public class Product {
    private String urunAdi;
    private double fiyat;
    private int yorumSayisi;
    private double puan;
    private int resimKaynak;

    public Product(String urunAdi, double fiyat, int yorumSayisi, double puan, int resimKaynak) {
        this.urunAdi = urunAdi;
        this.fiyat = fiyat;
        this.yorumSayisi = yorumSayisi;
        this.puan = puan;
        this.resimKaynak = resimKaynak;
    }

    // Getter ve Setter metotları
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

}

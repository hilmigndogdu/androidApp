package com.hilmigndogdu.androidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
public class CartManager {

    private static final String PREFERENCE_ADI = "sepetBilgileri";
    private static final String ANAHTAR_SEPET = "sepetUrunleri";
    private SharedPreferences sharedPreferences;

    public CartManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_ADI, Context.MODE_PRIVATE);
    }

    // Ürünü sepete ekle veya adet artır
    public void urunEkle(int urunId) {
        Map<Integer, Integer> sepet = sepetiGetir();
        int mevcutAdet = sepet.getOrDefault(urunId, 0);
        sepet.put(urunId, mevcutAdet + 1);
        sepetiKaydet(sepet);
        Log.d("SepetYonetici", "Ürün sepete eklendi: ID=" + urunId + ", Adet=" + (mevcutAdet + 1));
    }

    // Sepetten ürün çıkar
    public void urunCikar(int urunId) {
        Map<Integer, Integer> sepet = sepetiGetir();
        if (sepet.containsKey(urunId)) {
            sepet.remove(urunId);
            sepetiKaydet(sepet);
            Log.d("SepetYonetici", "Ürün sepetten çıkarıldı: ID=" + urunId);
        }
    }
    // Ürün adedini güncelle
    public void adetGuncelle(int urunId, int adet) {
        if (adet <= 0) {
            urunCikar(urunId);
            return;
        }
        Map<Integer, Integer> sepet = sepetiGetir();
        sepet.put(urunId, adet);
        sepetiKaydet(sepet);
        Log.d("SepetYonetici", "Ürün adedi güncellendi: ID=" + urunId + ", Yeni Adet=" + adet);
    }

    // Tüm sepeti temizle
    public void sepetiTemizle() {
        sharedPreferences.edit().remove(ANAHTAR_SEPET).apply();
        Log.d("SepetYonetici", "Sepet temizlendi.");
    }
    // Sepeti geri getir
    public Map<Integer, Integer> sepetiGetir() {
        Map<Integer, Integer> sepet = new HashMap<>();
        String sepetVerisi = sharedPreferences.getString(ANAHTAR_SEPET, null);
        if (sepetVerisi != null) {
            String[] urunler = sepetVerisi.split(",");
            for (String urun : urunler) {
                String[] parcalar = urun.split(":");
                int urunId = Integer.parseInt(parcalar[0]);
                int adet = Integer.parseInt(parcalar[1]);
                sepet.put(urunId, adet);
            }
        }
        return sepet;
    }

    private void sepetiKaydet(Map<Integer, Integer> sepet) {
        StringBuilder sepetVerisi = new StringBuilder();
        for (Map.Entry<Integer, Integer> urun : sepet.entrySet()) {
            sepetVerisi.append(urun.getKey()).append(":").append(urun.getValue()).append(",");
        }
        if (sepetVerisi.length() > 0) {
            sepetVerisi.deleteCharAt(sepetVerisi.length() - 1);  // Son virgülü kaldır
        }
        sharedPreferences.edit().putString(ANAHTAR_SEPET, sepetVerisi.toString()).apply();
    }

}

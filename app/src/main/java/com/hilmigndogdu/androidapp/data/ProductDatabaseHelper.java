package com.hilmigndogdu.androidapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hilmigndogdu.androidapp.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "productDatabase.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLO_URUN = "urun";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_URUN_ADI = "urunAdi";
    public static final String COLUMN_FIYAT = "fiyat";
    public static final String COLUMN_YORUM = "yorumSayisi";
    public static final String COLUMN_PUAN = "puan";
    public static final String COLUMN_RESIM = "resimKaynak";
    public static final String COLUMN_ACIKLAMA = "aciklama";
    public static final String COLUMN_KATEGORI = "kategori";
    private static final String TABLO_OLUSTUR =
            "CREATE TABLE " + TABLO_URUN + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_URUN_ADI + " TEXT NOT NULL, " +
                    COLUMN_FIYAT + " REAL NOT NULL, " +
                    COLUMN_YORUM + " INTEGER NOT NULL, " +
                    COLUMN_PUAN + " REAL NOT NULL, " +
                    COLUMN_RESIM + " INTEGER NOT NULL, " +
                    COLUMN_ACIKLAMA + " TEXT, " +
                    COLUMN_KATEGORI + " TEXT);";

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABLO_OLUSTUR);
            Log.d("Database Operations", "Ürün tablosu oluşturuldu: " + TABLO_URUN);
        } catch (Exception e) {
            Log.e("Database Error", "Veritabanı oluşturulurken hata: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            try {
                db.execSQL("ALTER TABLE " + TABLO_URUN + " ADD COLUMN " + COLUMN_KATEGORI + " TEXT;");
                Log.d("Database Operations", "Kategori sütunu eklendi.");
            } catch (Exception e) {
                Log.e("Database Error", "Kategori sütunu eklenirken hata: " + e.getMessage());
            }
        }
    }

    // Ürün Ekleme Metodu
    public void urunEkle(String urunAdi, double fiyat, int yorum, double puan, int resimKaynak, String aciklama, String kategori) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_URUN_ADI, urunAdi);
        values.put(COLUMN_FIYAT, fiyat);
        values.put(COLUMN_YORUM, yorum);
        values.put(COLUMN_PUAN, puan);
        values.put(COLUMN_RESIM, resimKaynak);
        values.put(COLUMN_ACIKLAMA, aciklama);
        values.put(COLUMN_KATEGORI, kategori);  // Kategori bilgisi eklendi

        long result = db.insert(TABLO_URUN, null, values);
        if (result == -1) {
            Log.e("DB_ERROR", "Ürün eklenemedi: " + urunAdi);
        } else {
            Log.d("DB_SUCCESS", "Ürün başarıyla eklendi: " + urunAdi);
        }
        db.close();
    }

    // Tüm Ürünleri Getir
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLO_URUN, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));  // ID'yi de alıyoruz
            String urunAdi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URUN_ADI));
            double fiyat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FIYAT));
            int yorumSayisi = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YORUM));
            double puan = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PUAN));
            int resimKaynak = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESIM));
            String aciklama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACIKLAMA));
            String kategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI));

            // ID parametresini de içeren yeni Product yapısı
            Product product = new Product(id, urunAdi, fiyat, yorumSayisi, puan, resimKaynak);
            product.setCategory(kategori);
            productList.add(product);
        }
        cursor.close();
        db.close();
        return productList;
    }


    // Kategoriye Göre Ürünleri Getir
    public List<Product> getProductsByCategory(String kategori) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLO_URUN + " WHERE " + COLUMN_KATEGORI + "=?", new String[]{kategori});

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));  // ID'yi de alıyoruz
            String urunAdi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URUN_ADI));
            double fiyat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FIYAT));
            int yorumSayisi = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YORUM));
            double puan = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PUAN));
            int resimKaynak = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESIM));
            String aciklama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACIKLAMA));
            String kategoriVeri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI));

            // ID parametresini de içeren yeni Product yapısı
            Product product = new Product(id, urunAdi, fiyat, yorumSayisi, puan, resimKaynak);
            product.setCategory(kategoriVeri);
            productList.add(product);
        }
        cursor.close();
        db.close();
        return productList;
    }


    public void resetProductDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ProductDatabaseHelper.TABLO_URUN);  // Ürün tablosundaki tüm verileri siler
        db.close();
        Log.d("Database", "Ürün veritabanı sıfırlandı.");
    }

    public Product getProductById(int urunId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Product product = null;

        String query = "SELECT * FROM " + TABLO_URUN + " WHERE " + COLUMN_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(urunId)});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String urunAdi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URUN_ADI));
            double fiyat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FIYAT));
            int yorumSayisi = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YORUM));
            double puan = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PUAN));
            int resimKaynak = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESIM));
            String aciklama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACIKLAMA));
            String kategori = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KATEGORI));

            product = new Product(id, urunAdi, fiyat, yorumSayisi, puan, resimKaynak);
            product.setCategory(kategori);
        }
        cursor.close();
        db.close();
        return product;
    }


}

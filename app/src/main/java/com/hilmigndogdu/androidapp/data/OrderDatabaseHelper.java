package com.hilmigndogdu.androidapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "orderDatabase.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLO_SIPARIS = "siparis";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TARIH = "tarih";
    public static final String COLUMN_URUNLER = "urunler";
    public static final String COLUMN_TOPLAM_FIYAT = "toplamFiyat";
    public static final String COLUMN_KONUM = "konum";
    public static final String COLUMN_ODEME_YONTEMI = "odemeYontemi";

    public OrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLO_SIPARIS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TARIH + " TEXT, " +
                COLUMN_URUNLER + " TEXT, " +
                COLUMN_TOPLAM_FIYAT + " REAL, " +
                COLUMN_KONUM + " TEXT, " +
                COLUMN_ODEME_YONTEMI + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLO_SIPARIS);
        onCreate(db);
    }

    // Sipariş ekleme metodu
    public void siparisEkle(String tarih, String urunler, double toplamFiyat, String konum, String odemeYontemi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TARIH, tarih);
        values.put(COLUMN_URUNLER, urunler);
        values.put(COLUMN_TOPLAM_FIYAT, toplamFiyat);
        values.put(COLUMN_KONUM, konum);
        values.put(COLUMN_ODEME_YONTEMI, odemeYontemi);
        db.insert(TABLO_SIPARIS, null, values);
        db.close();
    }

    // Tüm siparişleri getiren fonksiyon
    public List<String> getAllOrders() {
        List<String> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Sipariş bilgilerini çek
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLO_SIPARIS, null);
        while (cursor.moveToNext()) {
            String tarih = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TARIH));
            String urunler = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URUNLER));
            double toplamFiyat = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOPLAM_FIYAT));
            String konum = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KONUM));
            String odemeYontemi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ODEME_YONTEMI));

            // Sipariş bilgisini formatlayarak listeye ekle
            String orderInfo = "Tarih: " + tarih + "\nÜrünler: " + urunler + "\nToplam Fiyat: " + toplamFiyat +
                    "\nKonum: " + konum + "\nÖdeme Yöntemi: " + odemeYontemi;
            orderList.add(orderInfo);
        }

        cursor.close();
        db.close();
        return orderList;
    }

}

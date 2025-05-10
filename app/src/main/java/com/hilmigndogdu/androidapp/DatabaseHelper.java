package com.hilmigndogdu.androidapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_ADI = "kullaniciDatabase.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLO_KULLANICI = "kullanici";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KULLANICIADI = "kullanici";
    public static final String COLUMN_SIFRE = "sifre";
    public static final String COLUMN_EMAIL = "email";

    private static final String TABLO_OLUSTUR =
            "CREATE TABLE " + TABLO_KULLANICI + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_KULLANICIADI + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_SIFRE + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_ADI, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABLO_OLUSTUR);
            Log.d("Database Islemleri", "Tablo oluşturuldu: " + TABLO_KULLANICI);
        } catch (Exception e) {
            Log.e("Database Hata", "Veritabanı oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int eskiVersion, int yeniVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLO_KULLANICI);
            onCreate(db);
            Log.d("Database Islemleri", "Tablo güncellendi: " + TABLO_KULLANICI);
        } catch (Exception e) {
            Log.e("Database Error", "Veritabanı güncellenirken hata oluştu " + e.getMessage());
        }
    }

    public boolean kullaniciEkle(String kullaniciAdi, String email, String sifre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put(COLUMN_KULLANICIADI, kullaniciAdi);
        degerler.put(COLUMN_EMAIL, email);
        degerler.put(COLUMN_SIFRE, sifre);

        long result = db.insert(TABLO_KULLANICI, null, degerler);
        db.close();

        if (result == -1) {
            Log.e("DatabaseError", "Kullanıcı kaydı başarısız!");
            return false;
        } else {
            Log.d("DatabaseSuccess", "Kullanıcı kaydı başarılı!");
            return true;
        }
    }

    public boolean sifreGuncelle(String kullaniciAdi, String email, String yeniSifre) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kullanıcıyı bulmak için kullanıcı adı ve e-posta eşleştirmesi
        String query = "SELECT * FROM " + TABLO_KULLANICI + " WHERE " + COLUMN_KULLANICIADI + "=? AND " + COLUMN_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{kullaniciAdi, email});

        if (cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SIFRE, yeniSifre);

            int result = db.update(TABLO_KULLANICI, values, COLUMN_KULLANICIADI + "=? AND " + COLUMN_EMAIL + "=?", new String[]{kullaniciAdi, email});
            cursor.close();
            db.close();
            return result > 0;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }


    public boolean kullaniciAdiVarMi(String kullaniciAdi) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLO_KULLANICI + " WHERE " + COLUMN_KULLANICIADI + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{kullaniciAdi});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public boolean kullaniciKontrol(String kullaniciAdi, String sifre) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLO_KULLANICI + " WHERE " +
                COLUMN_KULLANICIADI + "=? AND " + COLUMN_SIFRE + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{kullaniciAdi, sifre});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }
}

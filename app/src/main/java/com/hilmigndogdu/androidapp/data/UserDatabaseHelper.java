package com.hilmigndogdu.androidapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_ADI = "userDatabase.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLO_KULLANICI = "kullanici";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ADSOYAD = "adSoyad";
    public static final String COLUMN_KULLANICIADI = "kullanici";
    public static final String COLUMN_SIFRE = "sifre";
    public static final String COLUMN_EMAIL = "email";

    private static final String TABLO_OLUSTUR =
            "CREATE TABLE " + TABLO_KULLANICI + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ADSOYAD + " TEXT NOT NULL, " +
                    COLUMN_KULLANICIADI + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_SIFRE + " TEXT NOT NULL);";

    public UserDatabaseHelper(Context context) {
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

    public boolean kullaniciEkle(String adSoyad, String kullaniciAdi, String email, String sifre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put(COLUMN_ADSOYAD, adSoyad);
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

    public List<String> kullanicilariGetir() {
        List<String> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLO_KULLANICI, null);


        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String kullaniciAdi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KULLANICIADI));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String sifre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIFRE));

                String userInfo = "ID: " + id + "\n" +
                        "İsim: " + kullaniciAdi + "\n" +
                        "Email: " + email + "\n" +
                        "Kullanıcı Adı: " + kullaniciAdi + "\n" +
                        "Şifre: " + sifre;

                userList.add(userInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public void resetUserDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + UserDatabaseHelper.TABLO_KULLANICI);  // Kullanıcı tablosundaki tüm verileri siler
        db.close();
        Log.d("Database", "Kullanıcı veritabanı sıfırlandı.");
    }

    public String getAdSoyadFromDatabase(String kullaniciAdi) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ADSOYAD + " FROM " + TABLO_KULLANICI + " WHERE " + COLUMN_KULLANICIADI + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{kullaniciAdi});

        String adSoyad = null;
        if (cursor.moveToFirst()) {
            adSoyad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADSOYAD));
        }
        cursor.close();
        db.close();
        return adSoyad;
    }

    public String getEmailFromDatabase(String kullaniciAdi) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_EMAIL + " FROM " + TABLO_KULLANICI + " WHERE " + COLUMN_KULLANICIADI + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{kullaniciAdi});

        String email = null;
        if (cursor.moveToFirst()) {
            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
        }
        cursor.close();
        db.close();
        return email;
    }


}

package com.hilmigndogdu.androidapp.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hilmigndogdu.androidapp.CartManager;
import com.hilmigndogdu.androidapp.data.ProductDatabaseHelper;
import com.hilmigndogdu.androidapp.R;

public class DetailActivity extends AppCompatActivity {

    private TextView urunAdi, fiyat, yorumSayisi, puan, aciklama;
    private ImageView resim;
    private Button spteklebtn;
    private int urunId;
    private CartManager sepetYonetici;
    private ImageView geriButon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        urunAdi = findViewById(R.id.titletxt);
        fiyat = findViewById(R.id.fiyattxt);
        aciklama = findViewById(R.id.aciklamatxt);
        resim = findViewById(R.id.imageView2);
        spteklebtn = findViewById(R.id.spteklebtn);

        sepetYonetici = CartManager.getInstance(this);

        String name = getIntent().getStringExtra("urunAdi");
        double price = getIntent().getDoubleExtra("fiyat", 0);
        int imageResource = getIntent().getIntExtra("resimKaynak", 0);


        // Veriyi veritabanından çekme
        String description = getProductDescription(name);
        urunId = getProductId(name);

        urunAdi.setText(name);
        fiyat.setText(String.format("$%.2f", price));
        aciklama.setText(description);
        resim.setImageResource(imageResource);

        geriButon = findViewById(R.id.imageView);
        geriButon.setOnClickListener(v -> onBackPressed());



        spteklebtn.setOnClickListener(v -> {
            if (urunId == 0) {
                Toast.makeText(this, "Ürün bilgisi alınamadı!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Sepete ekleme işlemi
                CartManager.getInstance(getApplicationContext()).urunEkle(urunId);
                Toast.makeText(this, "Ürün sepete eklendi!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Sepete ekleme hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    private String getProductDescription(String productName) {
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String description = "";
        String query = "SELECT aciklama FROM urun WHERE urunAdi = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productName});

        if (cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndexOrThrow("aciklama"));
        }
        cursor.close();
        db.close();

        return description;
    }

    // Veritabanından ürün ID'sini al
    private int getProductId(String productName) {
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int id = -1;

        String query = "SELECT _id FROM urun WHERE urunAdi = ?";
        Cursor cursor = db.rawQuery(query, new String[]{productName});

        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        }
        cursor.close();
        db.close();
        return id;
    }
}
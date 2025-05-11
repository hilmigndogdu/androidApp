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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        urunAdi = findViewById(R.id.titletxt);
        fiyat = findViewById(R.id.fiyattxt);
        aciklama = findViewById(R.id.aciklamatxt);
        resim = findViewById(R.id.imageView2);
        spteklebtn = findViewById(R.id.spteklebtn);

        sepetYonetici = new CartManager(this);

        String name = getIntent().getStringExtra("urunAdi");
        double price = getIntent().getDoubleExtra("fiyat", 0);
        int imageResource = getIntent().getIntExtra("resimKaynak", -1);


        // Veriyi veritabanından çekme
        String description = getProductDescription(name);
        urunId = getProductId(name);

        urunAdi.setText(name);
        fiyat.setText(String.format("$%.2f", price));
        aciklama.setText(description);

        // Hatalı kaynak kontrolü
        if (imageResource == -1) {
            // Eğer kaynak bulunamazsa varsayılan bir resim ata
            imageResource = R.drawable.mac;  // Varsayılan resim
        }
        resim.setImageResource(imageResource);


        // Satın Al butonuna tıklanınca ürünü sepete ekle
        spteklebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sepetYonetici.urunEkle(urunId);
                Toast.makeText(DetailActivity.this, "Ürün sepete eklendi!", Toast.LENGTH_SHORT).show();
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
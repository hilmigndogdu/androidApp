package com.hilmigndogdu.androidapp.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hilmigndogdu.androidapp.CartAdapter;
import com.hilmigndogdu.androidapp.CartManager;
import com.hilmigndogdu.androidapp.R;
import com.hilmigndogdu.androidapp.data.ProductDatabaseHelper;
import com.hilmigndogdu.androidapp.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView toplamTxt;
    private CartManager sepetYonetici;
    private List<Product> urunListesi;
    private CartAdapter sepetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.view3);
        toplamTxt = findViewById(R.id.totaltxt);

        sepetYonetici = new CartManager(this);
        urunListesi = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        urunListesi = urunleriGetir();
        sepetAdapter = new CartAdapter(this, urunListesi, sepetYonetici, this::toplamiGuncelle);
        recyclerView.setAdapter(sepetAdapter);

        toplamiGuncelle();
    }

    // Sepetteki ürünleri getir
    private List<Product> urunleriGetir() {
        List<Product> urunListesi = new ArrayList<>();
        Map<Integer, Integer> sepet = sepetYonetici.sepetiGetir();
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);

        for (Map.Entry<Integer, Integer> urun : sepet.entrySet()) {
            int urunId = urun.getKey();
            int adet = urun.getValue();
            Product product = dbHelper.getProductById(urunId);

            if (product != null) {
                product.setAdet(adet); // Ürün adedini ayarla
                urunListesi.add(product);
            }
        }
        return urunListesi;
    }

    // Toplam tutarı güncelle
    private void toplamiGuncelle() {
        double toplam = 0;
        for (Product urun : urunListesi) {
            toplam += urun.getFiyat() * urun.getAdet();
        }
        toplamTxt.setText(String.format("$%.2f", toplam));
        Toast.makeText(this, "Toplam güncellendi!", Toast.LENGTH_SHORT).show();
    }
}
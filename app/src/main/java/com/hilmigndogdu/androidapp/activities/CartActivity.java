package com.hilmigndogdu.androidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hilmigndogdu.androidapp.CartAdapter;
import com.hilmigndogdu.androidapp.CartManager;
import com.hilmigndogdu.androidapp.R;
import com.hilmigndogdu.androidapp.data.OrderDatabaseHelper;
import com.hilmigndogdu.androidapp.data.ProductDatabaseHelper;
import com.hilmigndogdu.androidapp.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView toplamTxt;
    private CartManager sepetYonetici;
    private List<Product> urunListesi;
    private CartAdapter sepetAdapter;
    ImageView geriButon;
    Button siparisVerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        toplamTxt = findViewById(R.id.totaltxt);
        siparisVerBtn = findViewById(R.id.cardsiparisVerBtn); // Düzeltme burada
        siparisVerBtn.setOnClickListener(v -> siparisVer());

        recyclerView = findViewById(R.id.view3);
        geriButon = findViewById(R.id.bckbtn);
        geriButon.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


        // Singleton yapısına uygun şekilde CartManager'ı çağır
        sepetYonetici = CartManager.getInstance(getApplicationContext());
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
                product.setAdet(adet);
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

    private void siparisVer() {

        // Eğer sepet boşsa sipariş verilmesin
        if (urunListesi == null || urunListesi.isEmpty()) {
            Toast.makeText(this, "Sepet boş, sipariş verilemez!", Toast.LENGTH_SHORT).show();
            return;
        }

        // TextView bileşenlerinden adres ve ödeme yöntemi bilgilerini al
        TextView adresTextView = findViewById(R.id.textView9);
        TextView odemeYontemiTextView = findViewById(R.id.textView12);

        String adres = adresTextView.getText().toString().trim();
        String odemeYontemi = odemeYontemiTextView.getText().toString().trim();

        // Sipariş tarihini al
        String tarih = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        // Sepetteki ürün bilgilerini al
        List<Product> urunListesi = urunleriGetir();
        StringBuilder urunler = new StringBuilder();
        double toplamFiyat = 0;

        for (Product urun : urunListesi) {
            urunler.append(urun.getUrunAdi())
                    .append(" - ")
                    .append(String.format("%.2f", urun.getFiyat()))
                    .append(" TL\n");
            toplamFiyat += urun.getFiyat();
        }

        // Veritabanına sipariş kaydet
        OrderDatabaseHelper orderDatabaseHelper = new OrderDatabaseHelper(this);
        orderDatabaseHelper.siparisEkle(tarih, urunler.toString(), toplamFiyat, adres, odemeYontemi);

        // Sipariş başarılı mesajı
        Toast.makeText(this, "Sipariş başarıyla verildi!", Toast.LENGTH_SHORT).show();

        CartManager.getInstance(this).sepetiTemizle();
        Intent intent = new Intent(CartActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("navigateTo", "explorer");
        startActivity(intent);
        finish();
    }



}
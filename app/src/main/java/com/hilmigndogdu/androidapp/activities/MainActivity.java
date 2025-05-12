package com.hilmigndogdu.androidapp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hilmigndogdu.androidapp.data.UserDatabaseHelper;
import com.hilmigndogdu.androidapp.fragments.ProfileFragment;
import com.hilmigndogdu.androidapp.fragments.WishlistFragment;
import com.hilmigndogdu.androidapp.models.Product;
import com.hilmigndogdu.androidapp.adapters.ProductAdapter;
import com.hilmigndogdu.androidapp.data.ProductDatabaseHelper;
import com.hilmigndogdu.androidapp.R;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private String currentUser;

    private int currentMenuItemId = R.id.menu_explorer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNavigationView = findViewById(R.id.alt_menu);
        bottomNavigationView.setSelectedItemId(R.id.menu_explorer);
// switch-case yerine if-else kullandık çünkü Java'da switch-case yapısında kullanılan ifadelerin sabit (constant) olması gerekir.
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Aynı sayfaya tekrar tıklanmışsa hiçbir şey yapma
            if (itemId == currentMenuItemId) return true;

            // Ana sayfaya dönme işlemi
            if (itemId == R.id.menu_explorer) {
                // Eğer zaten MainActivity'deysek bir şey yapma
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                }
                currentMenuItemId = itemId;
                return true;
            }

            // Fragment veya Activity'ye geçiş işlemleri
            if (itemId == R.id.menu_wishlist) {
                loadFragment(new WishlistFragment());
            } else if (itemId == R.id.menu_cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            } else if (itemId == R.id.menu_profile) {
                loadFragment(new ProfileFragment());
            }
            currentMenuItemId = itemId;
            return true;
        });






        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String adSoyad = sharedPreferences.getString("adSoyad", "Kullanıcı Adı");
        TextView isimTxt = findViewById(R.id.isimTxt);
        isimTxt.setText(adSoyad);

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        loadAllProducts();


        EditText aramaEdt = findViewById(R.id.aramaCubuguEdt);

        aramaEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Burada herhangi bir şey yapmamıza gerek yok
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Metin değiştiğinde burada işlemi yapacağız
                String aramaMetni = charSequence.toString(); // Kullanıcının yazdığı metni alıyoruz
                filtrele(aramaMetni); // Filtreleme metodunu çağırıyoruz
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Burada herhangi bir şey yapmamıza gerek yok
            }
        });
    }
    private void filtrele(String aramaMetni) {
        // Veritabanından tüm ürünleri alıyoruz
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);
        List<Product> filteredList = dbHelper.getAllProducts(); // Veritabanındaki tüm ürünleri al

        // Filtreleme işlemi: arama metni ürün adında varsa, o ürünü listeye ekliyoruz
        List<Product> searchResults = new ArrayList<>();
        for (Product product : filteredList) {
            if (product.getUrunAdi().toLowerCase().contains(aramaMetni.toLowerCase())) {
                searchResults.add(product); // Ürün adında arama metni varsa, ekle
            }
        }
        productAdapter.updateProductList(searchResults);
    }
    private void loadAllProducts() {
        ProductDatabaseHelper dbHelper = new ProductDatabaseHelper(this);
        productList = dbHelper.getAllProducts(); // Veritabanındaki tüm ürünleri al
        productAdapter.updateProductList(productList); // Adapter'ı güncelle
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)  // Geri tuşu ile geri dönmek için
                .commit();
    }
}
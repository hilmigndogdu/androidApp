package com.hilmigndogdu.androidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productList = new ArrayList<>();
        productList.add(new Product("Laptop", 123.45, 12, 4.6, R.drawable.pic1));
        productList.add(new Product("Telefon", 99.99, 20, 4.2, R.drawable.pic2));
        productList.add(new Product("Kulaklık", 45.75, 8, 4.8, R.drawable.pic3));
        productList.add(new Product("Oyun Konsolu", 399.99, 5, 4.9, R.drawable.pic1));
        productList.add(new Product("Klavye", 59.99, 15, 4.5, R.drawable.pic2));
        productList.add(new Product("Mouse", 19.99, 30, 4.7, R.drawable.pic3));

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // switch yapısıyla olmadı if yapınca oldu, Neden? Hocaya sor
        BottomNavigationView bottomNavigationView = findViewById(R.id.alt_menu);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_explorer) {
                // Keşfet sayfasında zaten olduğumuz için bir işlem yapma
                return true;
            } else if (itemId == R.id.menu_wishlist) {
                Intent wishListIntent = new Intent(MainActivity.this, WishlistActivity.class);
                startActivity(wishListIntent);
                return true;
            } else if (itemId == R.id.menu_cart) {
                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(cartIntent);
                return true;
            } else if (itemId == R.id.menu_profile) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });


    }


}
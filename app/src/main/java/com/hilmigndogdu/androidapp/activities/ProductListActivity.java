package com.hilmigndogdu.androidapp.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.hilmigndogdu.androidapp.R;
import com.hilmigndogdu.androidapp.data.ProductDatabaseHelper;
import com.hilmigndogdu.androidapp.models.Product;

import java.util.ArrayList;
import java.util.List;


public class ProductListActivity extends AppCompatActivity {

    private ListView listView;
    private ProductDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        listView = findViewById(R.id.productListView);
        dbHelper = new ProductDatabaseHelper(this);

        // Veritabanından ürünleri çekme
        List<Product> productList = dbHelper.getAllProducts();
        List<String> productNames = new ArrayList<>();

        for (Product product : productList) {
            String displayText = product.getUrunAdi() + " - " + product.getFiyat() + "₺";
            productNames.add(displayText);
        }

        // ListView'e ürünleri yükleme
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productNames);
        listView.setAdapter(adapter);
    }
}
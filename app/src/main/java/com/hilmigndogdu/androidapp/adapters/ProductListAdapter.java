package com.hilmigndogdu.androidapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hilmigndogdu.androidapp.models.Product;
import com.hilmigndogdu.androidapp.R;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Kontrolleri yap ve log ekle
        if (holder.urunAdi != null) {
            holder.urunAdi.setText(product.getUrunAdi());
        } else {
            Log.e("AdapterError", "urunAdi TextView is null");
        }

        if (holder.fiyat != null) {
            holder.fiyat.setText(String.format("$%.2f", product.getFiyat()));
        } else {
            Log.e("AdapterError", "fiyat TextView is null");
        }

        if (holder.puan != null) {
            holder.puan.setText("Rating: " + product.getPuan());
        } else {
            Log.e("AdapterError", "puan TextView is null");
        }

        if (holder.yorumSayisi != null) {
            holder.yorumSayisi.setText("Reviews: " + product.getYorumSayisi());
        } else {
            Log.e("AdapterError", "yorumSayisi TextView is null");
        }

        if (holder.aciklama != null) {
            holder.aciklama.setText(product.getUrunAdi());
        } else {
            Log.e("AdapterError", "aciklama TextView is null");
        }

        if (holder.resim != null) {
            holder.resim.setImageResource(product.getResimKaynak());
        } else {
            Log.e("AdapterError", "resim ImageView is null");
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView urunAdi, fiyat, puan, yorumSayisi, aciklama;
        ImageView resim;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            urunAdi = itemView.findViewById(R.id.populerUrunlerBaslikTxt);
            fiyat = itemView.findViewById(R.id.populerUrunlerFiyatTxt);
            puan = itemView.findViewById(R.id.populerUrunlerScoreTxt);
            yorumSayisi = itemView.findViewById(R.id.populerUrunlerYorumSayisiTxt);
            aciklama = itemView.findViewById(R.id.populerUrunlerBaslikTxt);
            resim = itemView.findViewById(R.id.populerUrunlerImg);

            // Log ekleyelim
            Log.d("ViewHolder", "TextView urunAdi: " + urunAdi);
            Log.d("ViewHolder", "TextView fiyat: " + fiyat);
            Log.d("ViewHolder", "TextView puan: " + puan);
            Log.d("ViewHolder", "TextView yorumSayisi: " + yorumSayisi);
            Log.d("ViewHolder", "TextView aciklama: " + aciklama);
            Log.d("ViewHolder", "ImageView resim: " + resim);

        }
    }
}

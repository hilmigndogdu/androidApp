package com.hilmigndogdu.androidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hilmigndogdu.androidapp.models.Product;
import com.hilmigndogdu.androidapp.R;
import com.hilmigndogdu.androidapp.activities.DetailActivity;

import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    public void updateProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();  // RecyclerView'u güncelle
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.urunAdi.setText(product.getUrunAdi());
        holder.fiyat.setText(String.format("$%.2f", product.getFiyat()));
        holder.yorumSayisi.setText(String.valueOf(product.getYorumSayisi()));
        holder.puan.setText(String.valueOf(product.getPuan()));
        holder.resim.setImageResource(R.drawable.ic_launcher_background);


        // Ürün tıklama olayı
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("urunAdi", product.getUrunAdi());
            intent.putExtra("fiyat", product.getFiyat());
            intent.putExtra("yorumSayisi", product.getYorumSayisi());
            intent.putExtra("puan", product.getPuan());
            intent.putExtra("resimKaynak", product.getResimKaynak());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView urunAdi, fiyat, yorumSayisi, puan;
        ImageView resim, yorumIkon, yildizIkon;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            urunAdi = itemView.findViewById(R.id.populerUrunlerBaslikTxt);
            fiyat = itemView.findViewById(R.id.populerUrunlerFiyatTxt);
            yorumSayisi = itemView.findViewById(R.id.populerUrunlerYorumSayisiTxt);
            puan = itemView.findViewById(R.id.populerUrunlerScoreTxt);
            resim = itemView.findViewById(R.id.populerUrunlerImg);
            yorumIkon = itemView.findViewById(R.id.populerUrunlerYorumImg);
            yildizIkon = itemView.findViewById(R.id.populerUrunlerYildizImg);
        }
    }

}

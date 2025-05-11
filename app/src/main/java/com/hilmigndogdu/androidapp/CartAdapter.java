package com.hilmigndogdu.androidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hilmigndogdu.androidapp.R;
import com.hilmigndogdu.androidapp.models.Product;
import java.util.List;
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.SepetViewHolder> {

    private Context context;
    private List<Product> urunListesi;
    private CartManager sepetYonetici;
    private Runnable toplamGuncelle;

    public CartAdapter(Context context, List<Product> urunListesi, CartManager sepetYonetici, Runnable toplamGuncelle) {
        this.context = context;
        this.urunListesi = urunListesi;
        this.sepetYonetici = sepetYonetici;
        this.toplamGuncelle = toplamGuncelle;
    }

    @NonNull
    @Override
    public SepetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_cart, parent, false);
        return new SepetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SepetViewHolder holder, int position) {
        Product urun = urunListesi.get(position);
        holder.urunAdi.setText(urun.getUrunAdi());
        holder.fiyat.setText(String.format("$%.2f", urun.getFiyat()));
        holder.adet.setText(String.valueOf(urun.getAdet()));

        holder.silBtn.setOnClickListener(v -> {
            sepetYonetici.urunCikar(urun.getId());
            urunListesi.remove(position);
            notifyItemRemoved(position);
            toplamGuncelle.run();
        });

        holder.artirBtn.setOnClickListener(v -> {
            sepetYonetici.adetGuncelle(urun.getId(), urun.getAdet() + 1);
            urun.setAdet(urun.getAdet() + 1);
            holder.adet.setText(String.valueOf(urun.getAdet()));
            toplamGuncelle.run();
        });

        holder.azaltBtn.setOnClickListener(v -> {
            if (urun.getAdet() > 1) {
                sepetYonetici.adetGuncelle(urun.getId(), urun.getAdet() - 1);
                urun.setAdet(urun.getAdet() - 1);
                holder.adet.setText(String.valueOf(urun.getAdet()));
                toplamGuncelle.run();
            }
        });
    }

    @Override
    public int getItemCount() {
        return urunListesi.size();
    }

    public static class SepetViewHolder extends RecyclerView.ViewHolder {
        TextView urunAdi, fiyat, adet,artirBtn, azaltBtn, silBtn;

        public SepetViewHolder(@NonNull View itemView) {
            super(itemView);
            urunAdi = itemView.findViewById(R.id.sepetBaslikTxt);
            fiyat = itemView.findViewById(R.id.sepetFiyatTxt);
            adet = itemView.findViewById(R.id.sepetAdetTxt);
            silBtn = itemView.findViewById(R.id.sepetSilTxt);
            artirBtn = itemView.findViewById(R.id.sepetArttirTxt);
            azaltBtn = itemView.findViewById(R.id.sepetAzaltTxt);
        }
    }
}

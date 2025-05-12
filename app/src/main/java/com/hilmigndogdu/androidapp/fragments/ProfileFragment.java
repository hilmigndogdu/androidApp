package com.hilmigndogdu.androidapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hilmigndogdu.androidapp.R;
import com.hilmigndogdu.androidapp.activities.LoginActivity;
import com.hilmigndogdu.androidapp.data.OrderDatabaseHelper;
import com.hilmigndogdu.androidapp.data.UserDatabaseHelper;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    private TextView sifreSifirlaTxt, bilgileriGuncelleTxt,adSoyadProfileTxt,emailProfileTxt,hesabiSilTxt,siparislerimTxt;
    private LinearLayout sifreSifirlaLayout, bilgilerGuncelleLayout,siparislerLayout;
    private EditText yeniSifreEditText, adSoyadEditText, emailEditText,kullaniciAdiEditText;
    private Button sifreKaydetBtn, bilgileriKaydetBtn;
    ListView siparisListView;
    OrderDatabaseHelper orderDatabaseHelper;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Bileşenleri tanımlama
        sifreSifirlaTxt = view.findViewById(R.id.sifreSifirlaTxt);
        sifreSifirlaLayout = view.findViewById(R.id.sifreSifirlaLayout);
        yeniSifreEditText = view.findViewById(R.id.yeniSifreEditText);
        sifreKaydetBtn = view.findViewById(R.id.sifreKaydetBtn);
        bilgileriGuncelleTxt = view.findViewById(R.id.bilgileriGuncelleTxt);
        bilgilerGuncelleLayout = view.findViewById(R.id.bilgilerGuncelleLayout);
        bilgileriKaydetBtn = view.findViewById(R.id.bilgileriKaydetBtn);
        adSoyadEditText = view.findViewById(R.id.adSoyadEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        kullaniciAdiEditText = view.findViewById(R.id.kullaniciAdiEditText);
        adSoyadProfileTxt = view.findViewById(R.id.adSoyadProfileTxt);
        emailProfileTxt = view.findViewById(R.id.emailProfileTxt);
        hesabiSilTxt = view.findViewById(R.id.hesabiSilTxt);
        siparislerimTxt = view.findViewById(R.id.siparislerimTxt);
        siparislerLayout = view.findViewById(R.id.siparislerLayout);
        siparisListView = view.findViewById(R.id.siparisListView);
        orderDatabaseHelper = new OrderDatabaseHelper(getContext());


        // Şifremi Sıfırla'ya tıklanınca pencereyi açma/kapatma
        sifreSifirlaTxt.setOnClickListener(v -> {
            if (sifreSifirlaLayout.getVisibility() == View.GONE) {
                sifreSifirlaLayout.setVisibility(View.VISIBLE);
            } else {
                sifreSifirlaLayout.setVisibility(View.GONE);
            }
        });

        // Kaydet butonuna tıklayınca şifre güncelleme
        sifreKaydetBtn.setOnClickListener(v -> {
            String yeniSifre = yeniSifreEditText.getText().toString().trim();
            if (yeniSifre.isEmpty()) {
                Toast.makeText(getContext(), "Lütfen yeni şifreyi giriniz!", Toast.LENGTH_SHORT).show();
            } else {
                sifreSifirlaLayout.setVisibility(View.GONE);
            }
        });

        // Bilgileri Güncelle'ye tıklayınca pencereyi açma/kapatma
        bilgileriGuncelleTxt.setOnClickListener(v -> {
            if (bilgilerGuncelleLayout.getVisibility() == View.GONE) {
                bilgilerGuncelleLayout.setVisibility(View.VISIBLE);
            } else {
                bilgilerGuncelleLayout.setVisibility(View.GONE);
            }
        });

        // Bilgileri Kaydet butonuna tıklayınca güncelleme
        bilgileriKaydetBtn.setOnClickListener(v -> {
            String adSoyad = adSoyadEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String kullaniciAdi = kullaniciAdiEditText.getText().toString().trim();

            if (adSoyad.isEmpty() || email.isEmpty() || kullaniciAdi.isEmpty()) {
                Toast.makeText(getContext(), "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
            } else {
                bilgilerGuncelleLayout.setVisibility(View.GONE);
            }
        });

        // Hesabı Sil'e tıklanınca işlem
        hesabiSilTxt.setOnClickListener(v -> {
            // AlertDialog oluşturma
            new androidx.appcompat.app.AlertDialog.Builder(getContext())
                    .setTitle("Hesabı Sil")
                    .setMessage("Hesabınızı silmek istediğinizden emin misiniz?")
                    .setPositiveButton("Evet", (dialog, which) -> {
                        // Kullanıcıyı LoginActivity'e yönlendirme
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish(); // ProfileFragment'i kapatma
                    })
                    .setNegativeButton("Hayır", (dialog, which) -> {
                        // Hiçbir işlem yapma, sadece pencereyi kapat
                        dialog.dismiss();
                    })
                    .show();
        });


        // Siparişlerime tıklanınca pencereyi aç/kapa
        siparislerimTxt.setOnClickListener(v -> {
            if (siparislerLayout.getVisibility() == View.GONE) {
                siparislerLayout.setVisibility(View.VISIBLE);
                siparisleriYukle();
            } else {
                siparislerLayout.setVisibility(View.GONE);
            }
        });

        return view;
    }
    // Siparişleri veritabanından al ve ListView'e yükle
    private void siparisleriYukle() {
        List<String> siparisListesi = orderDatabaseHelper.getAllOrders();

        if (siparisListesi.isEmpty()) {
            siparisListesi.add("Henüz bir sipariş yok.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, siparisListesi);
        siparisListView.setAdapter(adapter);
    }


}
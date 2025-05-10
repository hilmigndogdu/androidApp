package com.hilmigndogdu.androidapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;


public class RegisterActivity extends AppCompatActivity {

    EditText adSoyadEditText, emailEditText, kullaniciAdiEditText, sifreEditText, tekrarSifreEditText;
    Button kayitOlBtn;
    DatabaseHelper dbHelper;
    TextView girisYapTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        adSoyadEditText = findViewById(R.id.adSoyadEditText);
        emailEditText = findViewById(R.id.emailEditText);
        kullaniciAdiEditText = findViewById(R.id.kullaniciAdiEditText);
        sifreEditText = findViewById(R.id.sifreEditText);
        tekrarSifreEditText = findViewById(R.id.tekrarSifreEditText);
        kayitOlBtn = findViewById(R.id.kayitOlBtn);
        girisYapTxt = findViewById(R.id.girisYapTxt);

        dbHelper = new DatabaseHelper(this);

        girisYapTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        kayitOlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adSoyad = adSoyadEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String kullaniciAdi = kullaniciAdiEditText.getText().toString().trim();
                String sifre = sifreEditText.getText().toString().trim();
                String tekrarSifre = tekrarSifreEditText.getText().toString().trim();

                if (adSoyad.isEmpty() || email.isEmpty() || kullaniciAdi.isEmpty() || sifre.isEmpty() || tekrarSifre.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                } else if (!email.contains("@")) {
                    Toast.makeText(RegisterActivity.this, "Geçerli bir e-posta adresi girin", Toast.LENGTH_SHORT).show();
                } else if (!sifre.equals(tekrarSifre)) {
                    Toast.makeText(RegisterActivity.this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show();
                } else if (dbHelper.kullaniciAdiVarMi(kullaniciAdi)) {
                    Toast.makeText(RegisterActivity.this, "Bu kullanıcı adı zaten kayıtlı!", Toast.LENGTH_SHORT).show();
                } else {
                    // 🔥 GÜNCELLEME: Kullanıcı Ekleme İşlemi (3 parametre)
                    boolean kayitBasarili = dbHelper.kullaniciEkle(kullaniciAdi, email, sifre);
                    if (kayitBasarili) {
                        Toast.makeText(RegisterActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Kullanıcı kaydedilemedi", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}
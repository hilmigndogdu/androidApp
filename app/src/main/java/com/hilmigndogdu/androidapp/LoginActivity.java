package com.hilmigndogdu.androidapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    EditText etKullaniciAdi, etSifre;
    Button btnGiris;
    DatabaseHelper dbHelper;
    TextView txtKayit,txtSifre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etKullaniciAdi = findViewById(R.id.kullaniciAdiEdt);
        etSifre = findViewById(R.id.sifreEdtTxt);
        btnGiris = findViewById(R.id.girisYapBtn);
        txtKayit = findViewById(R.id.intentTxt);
        txtSifre = findViewById(R.id.textRegister);
        dbHelper = new DatabaseHelper(this);

        txtKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        txtSifre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kullaniciAdi = etKullaniciAdi.getText().toString();
                String sifre = etSifre.getText().toString();

                if (dbHelper.kullaniciKontrol(kullaniciAdi, sifre)) {
                    Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Kullanıcı Adı veya Şifre Hatalı", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
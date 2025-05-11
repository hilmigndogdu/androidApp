package com.hilmigndogdu.androidapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hilmigndogdu.androidapp.data.ProductDatabaseHelper;
import com.hilmigndogdu.androidapp.data.UserDatabaseHelper;
import com.hilmigndogdu.androidapp.R;


public class LoginActivity extends AppCompatActivity {

    EditText etKullaniciAdi, etSifre;
    Button btnGiris;
    UserDatabaseHelper dbHelper;
    TextView txtKayit,txtSifre;

    private UserDatabaseHelper userDbHelper;
    private ProductDatabaseHelper productDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDbHelper = new UserDatabaseHelper(this);
        productDbHelper = new ProductDatabaseHelper(this);

        // Reset veritabanları butonuna tıklama
        findViewById(R.id.resetButton).setOnClickListener(v -> resetDatabases());

        etKullaniciAdi = findViewById(R.id.kullaniciAdiEdt);
        etSifre = findViewById(R.id.sifreEdtTxt);
        btnGiris = findViewById(R.id.girisYapBtn);
        txtKayit = findViewById(R.id.intentTxt);
        txtSifre = findViewById(R.id.textRegister);
        dbHelper = new UserDatabaseHelper(this);

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

                    String adSoyad = dbHelper.getAdSoyadFromDatabase(kullaniciAdi);
                    String email = dbHelper.getEmailFromDatabase(kullaniciAdi);

                    SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("adSoyad", adSoyad);
                    editor.putString("kullaniciAdi", kullaniciAdi);
                    editor.putString("email", email);
                    editor.putString("sifre", sifre);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Kullanıcı Adı veya Şifre Hatalı", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    private void resetDatabases() {
        userDbHelper.resetUserDatabase();  // Kullanıcı veritabanını sıfırla
        productDbHelper.resetProductDatabase();  // Ürün veritabanını sıfırla
    }

}
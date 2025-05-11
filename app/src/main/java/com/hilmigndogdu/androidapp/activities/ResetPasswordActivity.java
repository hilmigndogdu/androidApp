package com.hilmigndogdu.androidapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hilmigndogdu.androidapp.data.UserDatabaseHelper;
import com.hilmigndogdu.androidapp.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputEditText kullaniciAdiEditText, emailEditText, yeniSifreEditText;
    private Button kaydetBtn;
    private TextView geriDönText;
    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        dbHelper = new UserDatabaseHelper(this);

        kullaniciAdiEditText = findViewById(R.id.kullaniciAdiEditText);
        emailEditText = findViewById(R.id.emailEditText);
        yeniSifreEditText = findViewById(R.id.yeniSifreEditText);
        kaydetBtn = findViewById(R.id.kaydetBtn);
        geriDönText = findViewById(R.id.geriDönText);

        geriDönText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        kaydetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kullaniciAdi = kullaniciAdiEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String yeniSifre = yeniSifreEditText.getText().toString().trim();

                if (kullaniciAdi.isEmpty() || email.isEmpty() || yeniSifre.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.contains("@")) {
                    Toast.makeText(ResetPasswordActivity.this, "Geçerli bir e-posta adresi girin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.sifreGuncelle(kullaniciAdi, email, yeniSifre)) {
                    Toast.makeText(ResetPasswordActivity.this, "Şifre başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Kullanıcı bulunamadı veya e-posta hatalı", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
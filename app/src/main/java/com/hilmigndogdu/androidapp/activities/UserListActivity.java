package com.hilmigndogdu.androidapp.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.hilmigndogdu.androidapp.data.UserDatabaseHelper;
import com.hilmigndogdu.androidapp.R;

import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private ListView userListView;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userListView = findViewById(R.id.userListView);
        userDatabaseHelper = new UserDatabaseHelper(this);

        // Kullanıcıları veritabanından çek
        List<String> users = userDatabaseHelper.kullanicilariGetir();

        // Verileri ArrayAdapter kullanarak göster
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        userListView.setAdapter(adapter);
    }
}
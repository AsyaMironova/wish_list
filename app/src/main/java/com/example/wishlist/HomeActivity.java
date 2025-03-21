package com.example.wishlist;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wishlist.ui.friends.FriendsFragment;
import com.example.wishlist.ui.profile.ProfileFragment;
import com.example.wishlist.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Установка начального фрагмента
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .commit();
        }

        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.purple_100));

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment())
                        .commit();
                return true;
            } else if (id == R.id.friends) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FriendsFragment())
                        .commit();
                return true;
            } else if (id == R.id.settings) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .commit();
                return true;
            } else {
                Toast.makeText(this, "Неизвестный пункт меню", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}
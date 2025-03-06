package com.example.wishlist;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (bottomNavigationView == null) {
            Log.e("HomeActivity", "bottomNavigationView is null");
        } else {
            Log.d("HomeActivity", "bottomNavigationView initialized");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Log.d("HomeActivity", "onNavigationItemSelected: " + item.getTitle());
                if (itemId == R.id.home) {
                    Log.d("HomeActivity", "Starting ProfileFragment");
                    replaceFragment(new ProfileFragment());
                    return true;
                } else if (itemId == R.id.friends) {
                    Log.d("HomeActivity", "Friends item selected");
                    replaceFragment(new FriendsFragment());
                    return true;
                } else if (itemId == R.id.settings) {
                    replaceFragment(new SettingsFragment());
                    return true;
                }
                return false;
            }
        });

        replaceFragment(new ProfileFragment()); // Изменено здесь
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
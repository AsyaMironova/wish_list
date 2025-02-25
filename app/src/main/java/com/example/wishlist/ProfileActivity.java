package com.example.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private LinearLayout linearLayoutSearch;
    private RecyclerView recyclerViewWishlists;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        appBarLayout = findViewById(R.id.appBarLayout);
        linearLayoutSearch = findViewById(R.id.linearLayoutSearch);
        recyclerViewWishlists = findViewById(R.id.recyclerViewWishlists);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        recyclerViewWishlists.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Добавить адаптер для RecyclerView

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    linearLayoutSearch.setVisibility(View.VISIBLE);
                } else if (verticalOffset == 0) {
                    linearLayoutSearch.setVisibility(View.GONE);
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            // TODO: Обработка нажатий на элементы BottomNavigationView
            return true;
        });

        Button buttonCreateWishlist = findViewById(R.id.buttonCreateWishlist);
        buttonCreateWishlist.setOnClickListener(v -> {
            // TODO: Обработка нажатия на кнопку создания виш-листа
        });
    }
}
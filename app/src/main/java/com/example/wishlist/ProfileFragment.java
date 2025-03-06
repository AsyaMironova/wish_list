package com.example.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private AppBarLayout appBarLayout;
    private LinearLayout linearLayoutSearch;
    private RecyclerView recyclerViewWishlists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        appBarLayout = view.findViewById(R.id.appBarLayout);
        linearLayoutSearch = view.findViewById(R.id.linearLayoutSearch);
        recyclerViewWishlists = view.findViewById(R.id.recyclerViewWishlists);

        recyclerViewWishlists.setLayoutManager(new LinearLayoutManager(getContext()));
        List<String> wishlists = new ArrayList<>();
        wishlists.add("Список желаний 1");
        wishlists.add("Список желаний 2");
        wishlists.add("Список желаний 3");
        WishlistAdapter adapter = new WishlistAdapter(wishlists);
        recyclerViewWishlists.setAdapter(adapter);

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

        Button buttonCreateWishlist = view.findViewById(R.id.buttonCreateWishlist);
        buttonCreateWishlist.setOnClickListener(v -> {
            // TODO: Обработка нажатия на кнопку создания виш-листа
        });

        return view;
    }
}
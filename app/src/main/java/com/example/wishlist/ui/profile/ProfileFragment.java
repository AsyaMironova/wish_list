package com.example.wishlist.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;
import com.example.wishlist.adapters.WishlistAdapter;
import com.example.wishlist.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerViewWishlists;
    private ProfileViewModel viewModel;
    private WishlistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerViewWishlists = view.findViewById(R.id.recyclerViewWishlists);
        recyclerViewWishlists.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new WishlistAdapter(wishlist -> {
            // Обработка клика по вишлисту
        });

        recyclerViewWishlists.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.getWishlistsLiveData().observe(getViewLifecycleOwner(), wishlists -> {
            adapter.setWishlists(wishlists);
        });

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            // Заполнение профиля данными
        });

        return view;
    }
}
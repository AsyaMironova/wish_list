package com.example.wishlist.ui.gifts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;
import com.example.wishlist.adapters.GiftsAdapter;
import com.example.wishlist.models.Gift;
import com.example.wishlist.viewmodels.GiftsViewModel;

import java.util.ArrayList;
import java.util.List;

public class GiftListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private GiftsAdapter giftAdapter;
    private GiftsViewModel viewModel;
    private String wishlistId;

    public static GiftListFragment newInstance(String wishlistId) {
        GiftListFragment fragment = new GiftListFragment();
        Bundle args = new Bundle();
        args.putString("wishlistId", wishlistId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        giftAdapter = new GiftsAdapter();
        recyclerView.setAdapter(giftAdapter);

        viewModel = new ViewModelProvider(this).get(GiftsViewModel.class);
        viewModel.getGifts().observe(getViewLifecycleOwner(), gifts -> {
            giftAdapter.setGiftList(gifts);
            progressBar.setVisibility(View.GONE);
        });

        wishlistId = getArguments() != null ? getArguments().getString("wishlistId") : "";
        viewModel.loadGifts(wishlistId);

        return view;
    }
}
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        wishlistId = getArguments() != null ? getArguments().getString("wishlistId") : "";

        recyclerView = view.findViewById(R.id.recyclerViewGifts);
        progressBar = view.findViewById(R.id.progressBarGifts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        giftAdapter = new GiftsAdapter(
                gift -> {
                    GiftBottomSheet bottomSheet = GiftBottomSheet.newInstance(gift, wishlistId);
                    bottomSheet.show(getParentFragmentManager(), "EditGiftBottomSheet");
                },
                gift -> {
                    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                            .setTitle("Удалить подарок?")
                            .setMessage("Вы уверены, что хотите удалить \"" + gift.getName() + "\"?")
                            .setPositiveButton("Удалить", (dialog, which) -> {
                                viewModel.deleteGift(wishlistId, gift.getId());
                            })
                            .setNegativeButton("Отмена", null)
                            .show();
                }
        );
        recyclerView.setAdapter(giftAdapter);

        viewModel = new ViewModelProvider(requireActivity()).get(GiftsViewModel.class);
        viewModel.getGifts().observe(getViewLifecycleOwner(), gifts -> {
            giftAdapter.setGiftList(gifts);
            progressBar.setVisibility(View.GONE);
        });

        FloatingActionButton buttonAddGift = view.findViewById(R.id.buttonAddGift);
        buttonAddGift.setOnClickListener(v -> {
            GiftBottomSheet bottomSheet = GiftBottomSheet.newInstance(null, wishlistId);
            bottomSheet.show(getParentFragmentManager(), "GiftBottomSheet");
        });

        progressBar.setVisibility(View.VISIBLE);
        viewModel.loadGifts(wishlistId);

        return view;
    }
}
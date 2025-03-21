package com.example.wishlist.ui.friends;

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
import com.example.wishlist.adapters.WishlistAdapter;
import com.example.wishlist.models.WishList;
import com.example.wishlist.ui.gifts.GiftListFragment;
import com.example.wishlist.viewmodels.FriendWishlistsViewModel;

import java.util.ArrayList;
import java.util.List;

public class FriendWishlistsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private WishlistAdapter wishlistAdapter;
    private FriendWishlistsViewModel viewModel;

    public static FriendWishlistsFragment newInstance(String friendId) {
        FriendWishlistsFragment fragment = new FriendWishlistsFragment();
        Bundle args = new Bundle();
        args.putString("friendId", friendId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_wishlists, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        wishlistAdapter = new WishlistAdapter(new WishlistAdapter.OnWishlistClickListener() {
            @Override
            public void onWishlistClick(WishList wishlist) {
                GiftListFragment giftListFragment = GiftListFragment.newInstance(wishlist.getId());
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, giftListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerView.setAdapter(wishlistAdapter);

        viewModel = new ViewModelProvider(this).get(FriendWishlistsViewModel.class);
        viewModel.getWishlists().observe(getViewLifecycleOwner(), wishlists -> {
            wishlistAdapter.setWishlistList(wishlists);
            progressBar.setVisibility(View.GONE);
        });

        String friendId = getArguments() != null ? getArguments().getString("friendId") : "";
        viewModel.loadWishlists(friendId);

        return view;
    }
}
package com.example.wishlist.ui.friends;

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
import com.example.wishlist.ui.gifts.GiftListFragment;
import com.example.wishlist.viewmodels.FriendsViewModel;

public class FriendWishlistsFragment extends Fragment {

    private FriendsViewModel viewModel;
    private WishlistAdapter wishlistAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_wishlists, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFriendWishlists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        wishlistAdapter = new WishlistAdapter(wishlist -> {
            GiftListFragment fragment = GiftListFragment.newInstance(wishlist.getId());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(wishlistAdapter);

        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        String friendId = getArguments() != null ? getArguments().getString("friendId") : null;
        if (friendId != null) {
            viewModel.loadFriendWishlists(friendId);
        }

        viewModel.getSelectedFriendWishlists().observe(getViewLifecycleOwner(), wishlists -> {
            wishlistAdapter.setWishlists(wishlists);
        });

        return view;
    }

    public static FriendWishlistsFragment newInstance(String friendId) {
        FriendWishlistsFragment fragment = new FriendWishlistsFragment();
        Bundle args = new Bundle();
        args.putString("friendId", friendId);
        fragment.setArguments(args);
        return fragment;
    }
}
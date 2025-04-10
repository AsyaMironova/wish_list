package com.example.wishlist.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wishlist.R;
import com.example.wishlist.adapters.WishlistAdapter;
import com.example.wishlist.models.WishList;
import com.example.wishlist.ui.gifts.GiftListFragment;
import com.example.wishlist.viewmodels.FriendsViewModel;

import java.util.List;

public class FriendWishlistsFragment extends Fragment {

    private FriendsViewModel viewModel;
    private WishlistAdapter wishlistAdapter;
    private com.example.wishlist.ui.gifts.GiftListFragment giftListFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_wishlists, container, false);
    }
    public static FriendWishlistsFragment newInstance(String friendId) {
        FriendWishlistsFragment fragment = new FriendWishlistsFragment();
        Bundle args = new Bundle();
        args.putString("friendId", friendId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFriendWishlists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        wishlistAdapter = new WishlistAdapter(wishlist -> {
            Fragment fragment = GiftListFragment.newInstance(wishlist.getId());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }, wishlist -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Удалить виш-лист?")
                    .setMessage("Вы уверены, что хотите удалить виш-лист \"" + wishlist.getName() + "\"?")
                    .setPositiveButton("Удалить", (dialog, which) -> viewModel.deleteWishlist(wishlist))
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        recyclerView.setAdapter(wishlistAdapter);

        viewModel.getFriendWishlists().observe(getViewLifecycleOwner(), wishlists -> {
            wishlistAdapter.setWishlists((List<WishList>) wishlists);
        });

        viewModel.fetchFriendWishlists();
    }
}

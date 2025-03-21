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
import com.example.wishlist.adapters.FriendsAdapter;
import com.example.wishlist.models.Friend;
import com.example.wishlist.viewmodels.FriendsViewModel;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FriendsAdapter friendsAdapter;
    private List<Friend> friendList = new ArrayList<>();
    private FriendsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        friendsAdapter = new FriendsAdapter(new FriendsAdapter.OnFriendClickListener() {
            @Override
            public void onFriendClick(Friend friend) {
                FriendWishlistsFragment fragment = FriendWishlistsFragment.newInstance(friend.getId());
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerView.setAdapter(friendsAdapter);

        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        viewModel.getFriends().observe(getViewLifecycleOwner(), friends -> {
            friendsAdapter.setFriendList(friends);
            progressBar.setVisibility(View.GONE);
        });

        viewModel.loadFriends();

        return view;
    }
}
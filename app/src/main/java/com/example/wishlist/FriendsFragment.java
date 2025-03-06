package com.example.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    private EditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);

        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<FriendsAdapter.Friend> friends = new ArrayList<>();
        friends.add(new FriendsAdapter.Friend("Иван Иванов", "@ivanov"));
        friends.add(new FriendsAdapter.Friend("Петр Петров", "@petrov"));
        friends.add(new FriendsAdapter.Friend("Анна Сидорова", "@sidorova"));

        FriendsAdapter adapter = new FriendsAdapter(friends);
        friendsRecyclerView.setAdapter(adapter);

        return view;
    }
}
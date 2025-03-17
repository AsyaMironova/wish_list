package com.example.wishlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    private FriendsAdapter friendsAdapter;
    private List<Friend> friendsList = new ArrayList<>(); // Изменено на List<Friend>
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsAdapter = new FriendsAdapter(friendsList);
        friendsRecyclerView.setAdapter(friendsAdapter);

        progressBar = view.findViewById(R.id.progressBar);

        db = FirebaseFirestore.getInstance();

        loadFriends();

        return view;
    }

    private void loadFriends() {
        if (progressBar == null){
            Log.e("FriendsFragment","progressBar is null");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        friendsList.clear();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Friend friend = document.toObject(Friend.class);
                            if (friend != null) {
                                friendsList.add(friend);
                            }
                        }
                        friendsAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("FriendsFragment", "Ошибка загрузки друзей", task.getException());
                        Toast.makeText(getContext(), "Ошибка загрузки друзей", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
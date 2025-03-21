package com.example.wishlist.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wishlist.models.Friend;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendsViewModel extends ViewModel {
    private final MutableLiveData<List<Friend>> friendsLiveData = new MutableLiveData<>();

    public LiveData<List<Friend>> getFriends() {
        return friendsLiveData;
    }

    public void loadFriends() {
        FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnSuccessListener(result -> {
                    List<Friend> list = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : result) {
                        Friend friend = doc.toObject(Friend.class);
                        list.add(friend);
                    }
                    friendsLiveData.setValue(list);
                });
    }
}
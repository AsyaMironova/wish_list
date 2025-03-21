package com.example.wishlist.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wishlist.models.WishList;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FriendWishlistsViewModel extends ViewModel {

    private final MutableLiveData<List<WishList>> wishlistsLiveData = new MutableLiveData<>(new ArrayList<>());
    private LiveData<Object> wishlists;

    public LiveData<List<WishList>> getWishlistsLiveData() {
        return wishlistsLiveData;
    }

    public void loadFriendWishlists(String friendId) {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(friendId)
                .collection("wishlists")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<WishList> wishlists = new ArrayList<>();
                    queryDocumentSnapshots.forEach(doc -> wishlists.add(doc.toObject(WishList.class)));
                    wishlistsLiveData.setValue(wishlists);
                });
    }

    public LiveData<Object> getWishlists() {
        return wishlists;
    }

    public void setWishlists(LiveData<Object> wishlists) {
        this.wishlists = wishlists;
    }

    public void loadWishlists(String friendId) {

    }
}

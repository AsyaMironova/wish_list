package com.example.wishlist.data;

import com.example.wishlist.models.Gift;
import com.example.wishlist.models.WishList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.example.wishlist.models.User;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    public FirebaseRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public String getCurrentUserId() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
    }

    public void getWishlists(Callback<List<WishList>> callback) {
        String userId = getCurrentUserId();
        if (userId == null) {
            callback.onFailure(new Exception("User not authenticated"));
            return;
        }

        db.collection("wishlists")
                .whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        callback.onFailure(error);
                        return;
                    }
                    List<WishList> list = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {
                        WishList wl = doc.toObject(WishList.class);
                        wl.setId(doc.getId());
                        list.add(wl);
                    }
                    callback.onSuccess(list);
                });
    }

    public void createWishlist(WishList wishlist, Callback<Void> callback) {
        db.collection("wishlists")
                .add(wishlist)
                .addOnSuccessListener(documentReference -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    public void getGifts(String wishlistId, Callback<List<Gift>> callback) {
        db.collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        callback.onFailure(error);
                        return;
                    }
                    List<Gift> giftList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {
                        Gift gift = doc.toObject(Gift.class);
                        giftList.add(gift);
                    }
                    callback.onSuccess(giftList);
                });
    }

    public interface Callback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    public void getCurrentUser(OnSuccessListener<User> listener) {
        db.collection("users")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    listener.onSuccess(user);
                });
    }

    public void getUserWishlists(OnSuccessListener<List<WishList>> listener) {
        db.collection("wishlists")
                .whereEqualTo("ownerId", auth.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<WishList> list = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        WishList wishList = doc.toObject(WishList.class);
                        list.add(wishList);
                    }
                    listener.onSuccess(list);
                });
    }
}
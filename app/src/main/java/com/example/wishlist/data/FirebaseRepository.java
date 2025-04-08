package com.example.wishlist.data;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wishlist.models.Gift;
import com.example.wishlist.models.User;
import com.example.wishlist.models.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseRepository {

    private final FirebaseFirestore firestore;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirebaseRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void getCurrentUser(final OnUserLoadedListener listener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            listener.onFailure(new IllegalStateException("User is not authenticated"));
            return;
        }
        String userId = firebaseUser.getUid();
        firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            user.setId(documentSnapshot.getId());
                            listener.onSuccess(user);
                        } else {
                            listener.onFailure(new Exception("User data is null"));
                        }
                    } else {
                        listener.onFailure(new Exception("User not found"));
                    }
                })
                .addOnFailureListener(listener::onFailure);
    }

    public void getUserInfo(String userId, final OnUserDataLoadedListener listener) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String name = document.getString("name");
                        String nickname = document.getString("nickname");
                        String about = document.getString("about");
                        listener.onSuccess(name, nickname, about);
                    } else {
                        listener.onFailure(new Exception("User not found"));
                    }
                })
                .addOnFailureListener(listener::onFailure);
    }

    public void uploadUserProfileImage(Uri imageUri, final OnImageUploadListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            listener.onFailure(new IllegalStateException("User not authenticated"));
            return;
        }

        String userId = user.getUid();
        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference("profile_images/" + userId + ".jpg");

        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();

                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(userId)
                                    .update("profileImageUrl", downloadUrl)
                                    .addOnSuccessListener(aVoid -> listener.onSuccess(downloadUrl))
                                    .addOnFailureListener(listener::onFailure);
                        })
                )
                .addOnFailureListener(listener::onFailure);
    }

    public void addWishlist(WishList wishlist, final OnWishlistCreatedListener listener) {
        firestore.collection("wishlists")
                .add(wishlist)
                .addOnSuccessListener(documentReference -> {
                    wishlist.setId(documentReference.getId());
                    listener.onSuccess(wishlist);
                })
                .addOnFailureListener(listener::onFailure);
    }

    public void getUserWishlists(String userId, final OnWishlistsLoadedListener listener) {
        firestore.collection("wishlists")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<WishList> wishlists = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        WishList wishList = document.toObject(WishList.class);
                        if (wishList != null) {
                            wishList.setId(document.getId());
                            wishlists.add(wishList);
                        }
                    }
                    listener.onSuccess(wishlists);
                })
                .addOnFailureListener(listener::onFailure);
    }

    public void getGiftsForWishlist(String wishlistId, final OnGiftsLoadedListener listener) {
        firestore.collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Gift> gifts = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Gift gift = document.toObject(Gift.class);
                        gifts.add(gift);
                    }
                    listener.onSuccess(gifts);
                })
                .addOnFailureListener(listener::onFailure);
    }

    public void getFriends(String userId, OnUsersLoadedListener listener) {
        db.collection("users").document(userId).collection("friends")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        users.add(doc.toObject(User.class));
                    }
                    listener.onSuccess(users);
                })
                .addOnFailureListener(listener::onFailure);
    }

    public void searchUsersByNickname(String nickname, String currentUserId, OnUsersLoadedListener listener) {
        db.collection("users")
                .whereEqualTo("nickname", nickname)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        if (!doc.getId().equals(currentUserId)) {
                            users.add(doc.toObject(User.class));
                        }
                    }
                    listener.onSuccess(users);
                })
                .addOnFailureListener(listener::onFailure);
    }

    public void addFriend(String userId, String friendId) {
        db.collection("users").document(userId).collection("friends").document(friendId)
                .set(new HashMap<>());
    }

    public void removeFriend(String userId, String friendId) {
        db.collection("users").document(userId).collection("friends").document(friendId)
                .delete();
    }

    // Interfaces
    public interface OnUserLoadedListener {
        void onSuccess(User user);
        void onFailure(Exception e);
    }

    public interface OnUserDataLoadedListener {
        void onSuccess(String name, String nickname, String about);
        void onFailure(Exception e);
    }

    public interface OnWishlistsLoadedListener {
        void onSuccess(List<WishList> wishlists);
        void onFailure(Exception e);
    }

    public interface OnGiftsLoadedListener {
        void onSuccess(List<Gift> gifts);
        void onFailure(Exception e);
    }

    public interface OnImageUploadListener {
        void onSuccess(String imageUrl);
        void onFailure(Exception e);
    }

    public interface OnWishlistCreatedListener {
        void onSuccess(WishList wishlist);
        void onFailure(Exception e);
    }

    public interface OnUsersLoadedListener {
        void onSuccess(List<User> users);
        void onFailure(Exception e);
    }

    public void updateUser(User user) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseRepo", "User updated"))
                .addOnFailureListener(e -> Log.e("FirebaseRepo", "Failed to update user", e));
    }
}
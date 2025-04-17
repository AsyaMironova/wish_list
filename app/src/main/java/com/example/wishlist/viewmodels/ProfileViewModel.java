package com.example.wishlist.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.wishlist.data.FirebaseRepository;
import com.example.wishlist.models.User;
import com.example.wishlist.models.WishList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final FirebaseRepository repository;
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<WishList>> wishlistsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);

    public ProfileViewModel() {
        repository = new FirebaseRepository();
        fetchUserAndWishlists();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<List<WishList>> getWishlistsLiveData() {
        return wishlistsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<String> getUserNickname() {
        return Transformations.map(userLiveData, User::getNickname);
    }

    public LiveData<String> getUserId() {
        return Transformations.map(userLiveData, User::getUserId);
    }

    public LiveData<String> getUserAbout() {
        return Transformations.map(userLiveData, User::getAbout);
    }

    public LiveData<String> getProfileImageUrl() {
        return Transformations.map(userLiveData, User::getProfileImageUrl);
    }

    public LiveData<List<WishList>> getWishlists() {
        return wishlistsLiveData;
    }

    public void fetchUser() {
        fetchUserAndWishlists();
    }

    public void fetchUserAndWishlists() {
        isLoading.setValue(true);
        repository.getCurrentUser(new FirebaseRepository.OnUserLoadedListener() {
            @Override
            public void onSuccess(User user) {
                userLiveData.setValue(user);
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                fetchWishlists(uid);
            }

            @Override
            public void onFailure(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage());
            }
        });
    }

    public void fetchWishlists(String userId) {
        Log.d("ProfileVM", "Fetching wishlists for userId: " + userId);
        repository.getUserWishlists(userId, new FirebaseRepository.OnWishlistsLoadedListener() {
            @Override
            public void onSuccess(List<WishList> wishlists) {
                Log.d("ProfileVM", "Wishlists loaded: " + wishlists.size());
                wishlistsLiveData.setValue(wishlists);
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage());
            }
        });
    }

    public void createWishlist(WishList wishlist) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        wishlist.setUserId(uid);
        addWishlist(wishlist);
    }

    public void addWishlist(final WishList wishlist) {
        isLoading.setValue(true);
        repository.addWishlist(wishlist, new FirebaseRepository.OnWishlistCreatedListener() {
            @Override
            public void onSuccess(WishList newWishlist) {
                fetchWishlists(newWishlist.getUserId());
            }

            @Override
            public void onFailure(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage());
            }
        });
    }

    public void updateNickname(String nickname) {
        User current = userLiveData.getValue();
        if (current != null) {
            current.setNickname(nickname);
            repository.updateUser(current);
        }
    }

    public void updateUserId(String userId) {
        User current = userLiveData.getValue();
        if (current != null) {
            current.setUserId(userId);
            repository.updateUser(current);
        }
    }

    public void updateAbout(String about) {
        User current = userLiveData.getValue();
        if (current != null) {
            current.setAbout(about);
            repository.updateUser(current);
        }
    }

    public void uploadProfileImage(Uri imageUri) {
        repository.uploadUserProfileImage(imageUri, new FirebaseRepository.OnImageUploadListener() {
            @Override
            public void onSuccess(String imageUrl) {
                // Можно обновить userLiveData, если нужно
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("ProfileViewModel", "Image upload failed", e);
            }
        });
    }

    public void deleteWishlist(WishList wishlist) {
        repository.deleteWishlist(wishlist, new FirebaseRepository.OnWishlistDeletedListener() {
            @Override
            public void onSuccess() {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                fetchWishlists(uid);
            }

            @Override
            public void onFailure(Exception e) {
                error.setValue(e.getMessage());
            }
        });
    }

    public void validateUserId(String userId, OnUserIdValidationListener listener) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    boolean isTaken = false;
                    for (QueryDocumentSnapshot doc : snapshot) {
                        if (!doc.getId().equals(currentUid)) {
                            isTaken = true;
                            break;
                        }
                    }
                    listener.onResult(!isTaken);
                })
                .addOnFailureListener(e -> listener.onResult(false));
    }

    public interface OnUserIdValidationListener {
        void onResult(boolean isFree);
    }
}
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
        return Transformations.map(userLiveData, User::getImageUrl);
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
                fetchWishlists(user.getId());
            }

            @Override
            public void onFailure(Exception e) {
                isLoading.setValue(false);
                error.setValue(e.getMessage());
            }
        });
    }

    public void fetchWishlists(String userId) {
        repository.getUserWishlists(userId, new FirebaseRepository.OnWishlistsLoadedListener() {
            @Override
            public void onSuccess(List<WishList> wishlists) {
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
                // Update LiveData if necessary
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("ProfileViewModel", "Image upload failed", e);
            }
        });
    }
}
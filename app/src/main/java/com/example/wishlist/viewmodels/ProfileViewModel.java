package com.example.wishlist.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wishlist.data.FirebaseRepository;
import com.example.wishlist.models.User;
import com.example.wishlist.models.WishList;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final FirebaseRepository repository = new FirebaseRepository();

    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<WishList>> wishlistsLiveData = new MutableLiveData<>();

    public ProfileViewModel() {
        loadUser();
        loadWishlists();
    }

    private void loadUser() {
        repository.getCurrentUser(user -> userLiveData.setValue(user));
    }

    private void loadWishlists() {
        repository.getUserWishlists(wishlists -> wishlistsLiveData.setValue(wishlists));
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<List<WishList>> getWishlistsLiveData() {
        return wishlistsLiveData;
    }
}
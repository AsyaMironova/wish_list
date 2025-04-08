package com.example.wishlist.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wishlist.data.FirebaseRepository;
import com.example.wishlist.models.Friend;
import com.example.wishlist.models.User;
import com.example.wishlist.models.WishList;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class FriendsViewModel extends ViewModel {

    private final FirebaseRepository repository = new FirebaseRepository();

    private final MutableLiveData<List<User>> friendsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<WishList>> selectedFriendWishlists = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private LiveData<Object> friends;

    public LiveData<List<User>> getFriendsLiveData() {
        return friendsLiveData;
    }

    public LiveData<Boolean> getLoadingState() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void fetchFriends(String currentUserId) {
        isLoading.setValue(true);
        repository.getFriends(currentUserId, new FirebaseRepository.OnUsersLoadedListener() {
            @Override
            public void onSuccess(List<User> users) {
                friendsLiveData.setValue(users);
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Exception e) {
                error.setValue(e.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public void searchUsersByNickname(String nickname, String currentUserId) {
        isLoading.setValue(true);
        repository.searchUsersByNickname(nickname, currentUserId, new FirebaseRepository.OnUsersLoadedListener() {
            @Override
            public void onSuccess(List<User> users) {
                friendsLiveData.setValue(users);
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Exception e) {
                error.setValue(e.getMessage());
                isLoading.setValue(false);
            }
        });
    }

    public LiveData<List<WishList>> getSelectedFriendWishlists() {
        return selectedFriendWishlists;
    }

    public void loadFriendWishlists(String friendId) {
        repository.getUserWishlists(friendId, new FirebaseRepository.OnWishlistsLoadedListener() {
            @Override
            public void onSuccess(List<WishList> wishlists) {
                selectedFriendWishlists.setValue(wishlists);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FriendsViewModel", "Error loading wishlists", e);
            }
        });
    }

    public void addFriend(String friendId) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        repository.addFriend(currentUserId, friendId);
    }

    public void removeFriend(String friendId) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        repository.removeFriend(currentUserId, friendId);
    }

    public LiveData<List<Friend>> getFriends() {
        MutableLiveData<List<Friend>> result = new MutableLiveData<>();
        List<Friend> converted = new ArrayList<>();
        List<User> originalUsers = friendsLiveData.getValue(); // <- ваш внутренний LiveData список пользователей
        if (originalUsers != null) {
            for (User user : originalUsers) {
                Friend friend = new Friend();
                friend.setId(user.getId());
                friend.setNickname(user.getNickname());
                friend.setName(user.getName());
                friend.setAbout(user.getAbout());
                friend.setProfileImageUrl(user.getProfileImageUrl());
                converted.add(friend);
            }
        }
        result.setValue(converted);
        return result;
    }

    public void setFriends(LiveData<Object> friends) {
        this.friends = friends;
    }

    public void loadFriends() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fetchFriends(currentUserId);
    }
}
package com.example.wishlist.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wishlist.models.Gift;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GiftsViewModel extends ViewModel {
    private final MutableLiveData<List<Gift>> gifts = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Gift>> getGifts() {
        return gifts;
    }

    public void setGifts(List<Gift> giftList) {
        gifts.setValue(giftList);
    }

    public void loadGifts(String wishlistId) {

    }

    public void deleteGift(String wishlistId, String giftId) {
        FirebaseFirestore.getInstance()
                .collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .document(giftId)
                .delete()
                .addOnSuccessListener(unused -> {
                    // Можно добавить вывод Snackbar или лог
                })
                .addOnFailureListener(e -> {
                    Log.e("GiftsViewModel", "Ошибка удаления подарка", e);
                });
    }
}
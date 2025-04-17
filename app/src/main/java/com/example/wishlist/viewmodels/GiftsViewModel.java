package com.example.wishlist.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wishlist.models.Gift;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GiftsViewModel extends ViewModel {

    private final MutableLiveData<List<Gift>> gifts = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Gift>> getGifts() {
        return gifts;
    }

    public void loadGifts(String wishlistId) {
        FirebaseFirestore.getInstance()
                .collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Gift> loadedGifts = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Gift gift = doc.toObject(Gift.class);
                        if (gift != null) {
                            gift.setId(doc.getId());
                            loadedGifts.add(gift);
                        }
                    }
                    gifts.setValue(loadedGifts);
                })
                .addOnFailureListener(e -> Log.e("GiftsViewModel", "Ошибка загрузки подарков", e));
    }

    public void deleteGift(String wishlistId, String giftId) {
        FirebaseFirestore.getInstance()
                .collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .document(giftId)
                .delete()
                .addOnSuccessListener(unused -> Log.d("GiftsViewModel", "Подарок удален"))
                .addOnFailureListener(e -> Log.e("GiftsViewModel", "Ошибка удаления подарка", e));
    }
}
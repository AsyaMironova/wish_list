package com.example.wishlist.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wishlist.models.Gift;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GiftsViewModel extends ViewModel {

    private final MutableLiveData<List<Gift>> gifts = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Gift>> getGifts() {  // <-- Переименовано
        return gifts;
    }

    public void loadGifts(String wishlistId) { // <-- Переименовано
        FirebaseFirestore.getInstance()
                .collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Gift> giftList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Gift gift = doc.toObject(Gift.class);
                        gift.setId(doc.getId());
                        giftList.add(gift);
                    }
                    gifts.setValue(giftList);
                });
    }

    public void deleteGift(String wishlistId, String giftId) {
        FirebaseFirestore.getInstance()
                .collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .document(giftId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    List<Gift> currentList = gifts.getValue();
                    if (currentList != null) {
                        List<Gift> updatedList = new ArrayList<>(currentList);
                        updatedList.removeIf(g -> g.getId().equals(giftId));
                        gifts.setValue(updatedList);
                    }
                });
    }

    public void addGift(String wishlistId, Gift gift) {
        FirebaseFirestore.getInstance()
                .collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .add(gift)
                .addOnSuccessListener(documentReference -> {
                    gift.setId(documentReference.getId());
                    List<Gift> currentList = gifts.getValue();
                    if (currentList != null) {
                        List<Gift> updatedList = new ArrayList<>(currentList);
                        updatedList.add(gift);
                        gifts.setValue(updatedList);
                    }
                });
    }

    public void updateGift(String wishlistId, Gift gift) {
        FirebaseFirestore.getInstance()
                .collection("wishlists")
                .document(wishlistId)
                .collection("gifts")
                .document(gift.getId())
                .set(gift)
                .addOnSuccessListener(unused -> {
                    List<Gift> currentList = gifts.getValue();
                    if (currentList != null) {
                        List<Gift> updatedList = new ArrayList<>(currentList);
                        for (int i = 0; i < updatedList.size(); i++) {
                            if (updatedList.get(i).getId().equals(gift.getId())) {
                                updatedList.set(i, gift);
                                break;
                            }
                        }
                        gifts.setValue(updatedList);
                    }
                });
    }
}
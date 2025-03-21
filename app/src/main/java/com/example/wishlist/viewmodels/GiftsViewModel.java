package com.example.wishlist.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wishlist.models.Gift;

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
}
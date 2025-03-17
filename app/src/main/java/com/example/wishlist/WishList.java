package com.example.wishlist;

import java.util.List;

public class WishList {
    private String wishlistId;
    private String userId;
    private String name;
    private List<Gift> gifts;

    public WishList() {}

    public WishList(String wishlistId, String userId, String name, List<Gift> gifts) {
        this.wishlistId = wishlistId;
        this.userId = userId;
        this.name = name;
        this.gifts = gifts;
    }

    public String getWishlistId() { return wishlistId; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public List<Gift> getGifts() { return gifts; }
}
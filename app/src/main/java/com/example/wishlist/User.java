package com.example.wishlist;

import java.util.List;

public class User {
    private String userId;
    private String name;
    private String email;
    private List<String> friends; // Список ID друзей
    private List<String> wishlists; // Список ID виш-листов

    public User() {} // Конструктор без параметров

    public User(String userId, String name, String email, List<String> friends, List<String> wishlists) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.friends = friends;
        this.wishlists = wishlists;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getFriends() { return friends; }
    public List<String> getWishlists() { return wishlists; }
}
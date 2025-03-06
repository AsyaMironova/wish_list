package com.example.wishlist;

public class Friend {
    private String name;
    private String username;

    public Friend(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
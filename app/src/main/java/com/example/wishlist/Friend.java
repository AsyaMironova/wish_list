package com.example.wishlist;

public class Friend {
    private String name;
    private String username;

    public Friend() {} // Пустой конструктор

    public Friend(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
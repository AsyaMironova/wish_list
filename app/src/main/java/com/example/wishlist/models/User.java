package com.example.wishlist.models;

public class User {
    private String name;
    private String nickname;

    public User() {}

    public User(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }
    public String getUsername() {
        return nickname; // или любое другое поле, которое используете
    }
}
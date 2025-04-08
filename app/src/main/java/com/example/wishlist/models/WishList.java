package com.example.wishlist.models;

public class WishList {

    private String id;
    private String name;
    private String description;
    private String userId;

    // Обязательный пустой конструктор для Firebase
    public WishList() {}

    public WishList(String id, String name) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
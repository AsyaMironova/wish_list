package com.example.wishlist.models;

public class WishList {
    private String id;
    private String name;
    private String description;
    private String privacy;
    private String userId;

    public WishList() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrivacy() { return privacy; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public WishList(String name, String description, String privacy) {
        this.name = name;
        this.description = description;
        this.privacy = privacy;
    }
}
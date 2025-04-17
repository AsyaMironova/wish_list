package com.example.wishlist.models;

public class Gift {
    private String id;
    private String name;
    private String description;
    private String link;
    private String price;

    private String title;

    public Gift() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTitle() {
        return title;
    }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}
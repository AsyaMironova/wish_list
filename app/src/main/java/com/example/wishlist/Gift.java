package com.example.wishlist;

public class Gift {
    private String name;
    private String description;
    private String link;
    private String price;

    public Gift() {}  // Пустой конструктор нужен для Firebase

    public Gift(String name, String description, String link, String price) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.price = price;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}
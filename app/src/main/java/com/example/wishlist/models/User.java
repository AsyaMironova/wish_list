package com.example.wishlist.models;

public class User {

    private String id;
    private String name;
    private String email;
    private String profileImageUrl;
    private String about;
    private String nickname;
    private String user_id;

    public User() {
        // Firestore требует пустой конструктор
    }

    public User(String id, String name, String email, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    // ID документа Firestore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Отображаемое имя пользователя
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Ссылка на изображение профиля
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    // Описание
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    // Никнейм (для отображения)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // Юзер ID (уникальный id для профиля, задаётся пользователем)
    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }
}
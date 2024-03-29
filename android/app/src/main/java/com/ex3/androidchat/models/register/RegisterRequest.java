package com.ex3.androidchat.models.register;

public class RegisterRequest {
    String id, name, password, profileImage, server;

    public RegisterRequest() {
    }

    public RegisterRequest(String id, String name, String password, String profileImage, String server) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.profileImage = profileImage;
        this.server = server;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getServer() {
        return server;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setServer(String server) {
        this.server = server;
    }
}

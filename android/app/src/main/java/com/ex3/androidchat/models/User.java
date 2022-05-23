package com.ex3.androidchat.models;

import java.time.LocalDateTime;

public class User {
    String id, password, last, name, server, profileImage;
    LocalDateTime lastdate;
    public User() { }

    public User(String id, String password, String last, String profileImage, String name, String server, LocalDateTime lastdate) {
        this.id = id;
        this.password = password;
        this.last = last;
        this.name = name;
        this.profileImage = profileImage;
        this.lastdate = lastdate;
        this.server = server;
    }

    public User(String id, String password, String name, String server) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.profileImage = "/images/default.jpg";
        this.last = null;
        this.lastdate = null;
        this.server = server;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getLast() {
        return last;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public LocalDateTime getLastdate() {
        return lastdate;
    }
}

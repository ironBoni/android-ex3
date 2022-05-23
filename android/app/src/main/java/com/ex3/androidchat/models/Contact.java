package com.ex3.androidchat.models;

import java.time.LocalDateTime;

public class Contact {
    String id, name, server, last, profileImage;
    LocalDateTime lastdate;

    public Contact(String id, String name, String server, String last, String profileImage, LocalDateTime lastdate) {
        this.id = id;
        this.name = name;
        this.server = server;
        this.last = last;
        this.profileImage = profileImage;
        this.lastdate = lastdate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public String getLast() {
        return last;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public LocalDateTime getLastdate() {
        return lastdate;
    }
}

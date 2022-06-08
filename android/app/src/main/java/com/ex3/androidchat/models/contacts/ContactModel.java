package com.ex3.androidchat.models.contacts;

import java.time.LocalDateTime;

public class ContactModel {
    String id, name, server, last, profileImage;
    LocalDateTime lastdate;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setLastdate(LocalDateTime lastdate) {
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

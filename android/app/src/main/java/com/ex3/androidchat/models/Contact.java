package com.ex3.androidchat.models;

import com.ex3.androidchat.R;

import java.time.LocalDateTime;

public class Contact {
    String id, name, server, last;
    int profileImage;
    String lastdate;

    public Contact(String id, String name, String server, String last, int profileImage, String lastdate) {
        this.id = id;
        this.name = name;
        this.server = server;
        this.last = last;
        this.profileImage = profileImage;
        this.lastdate = lastdate;
    }

    public Contact(String id, String name, String server) {
        this.id = id;
        this.name = name;
        this.server = server;
        this.last = null;
        this.lastdate = null;
        this.profileImage = R.drawable.default_avatar;
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

    public int getProfileImage() {
        return profileImage;
    }

    public String getLastdate() {
        return lastdate;
    }
}

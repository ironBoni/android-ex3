package com.ex3.androidchat.models;

import android.content.Context;

import com.ex3.androidchat.Client;
import com.ex3.androidchat.R;

import java.time.LocalDateTime;

public class Contact {
    String id, name, server, last;
    String profileImage;
    String lastdate;

    public Contact(String id, String name, String server, String last, String profileImage, String lastdate) {
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
        this.profileImage = Client.defaultImage;
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

    public String getLastdate() {
        return lastdate;
    }
}

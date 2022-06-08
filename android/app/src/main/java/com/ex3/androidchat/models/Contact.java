package com.ex3.androidchat.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ex3.androidchat.Client;
@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    String id;
    String name, server, last;
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

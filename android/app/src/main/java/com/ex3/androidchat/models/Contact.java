package com.ex3.androidchat.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ex3.androidchat.Client;

import java.util.Random;

@Entity
public class Contact {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String contactId;
    public String name, server, last;
    public String profileImage;
    public String lastdate;

    public Contact() { }
    @Ignore
    public Contact(String contactId, String name, String server, String last, String profileImage, String lastdate) {
        this.id = new Random().nextInt(900000) + 100000;
        this.contactId = contactId;
        this.name = name;
        this.server = server;
        this.last = last;
        this.profileImage = profileImage;
        this.lastdate = lastdate;
    }

    @Ignore
    public Contact(int id, String contactId, String name, String server) {
        this.id = id;
        this.contactId = contactId;
        this.name = name;
        this.server = server;
        this.last = null;
        this.lastdate = null;
        this.profileImage = Client.defaultImage;
    }


    @Ignore
    public Contact(String contactId, String name, String server) {
        this.id = new Random().nextInt(900000) + 100000;
        this.contactId = contactId;
        this.name = name;
        this.server = server;
        this.last = null;
        this.lastdate = null;
        this.profileImage = Client.defaultImage;
    }

    public int getId() {
        return id;
    }
    public String getContactId() { return contactId; }

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

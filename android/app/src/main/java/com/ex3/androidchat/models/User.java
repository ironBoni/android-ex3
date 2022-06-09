package com.ex3.androidchat.models;

import java.util.ArrayList;

public class User {
    public String id, password, last, name, server, profileImage;

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    ArrayList<Contact> contacts;
    String lastdate;
    public User() { }

    public User(String id, String password, String last, String profileImage, String name, String server, String lastdate) {
        this.id = id;
        this.password = password;
        this.last = last;
        this.name = name;
        this.profileImage = profileImage;
        this.lastdate = lastdate;
        this.server = server;
        this.contacts = new ArrayList<>();
    }

    public User(String id, String name, String password, String image) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.profileImage = image;
        this.last = null;
        this.lastdate = null;
        this.server = "http://localhost:5186";
        this.contacts = new ArrayList<>();
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

    public String getLastdate() {
        return lastdate;
    }

    public String getServer() {
        return server;
    }


    public String getFullServerUrl(String url)
    {
        if (!url.endsWith("/"))
            url = url + "/";
        if (!url.startsWith("http://"))
            url = "http://" + url;
        return url;
    }

    public void setServer(String server) {
        this.server = getFullServerUrl(server);
    }
}

package com.ex3.androidchat.models.contacts;

import com.ex3.androidchat.models.Contact;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserModel {
    public String id;

    @SerializedName("name")
    public String name;
    public String password, profileImage, server;

    @SerializedName("contacts")
    public ArrayList<Contact> contacts;

    public UserModel() {
    }

    public UserModel(String username, String nickname, String password, String profileImage, String server, ArrayList<Contact> contacts) {
        this.id = username;
        this.name = nickname;
        this.password = password;
        this.profileImage = profileImage;
        this.server = server;
        this.contacts = contacts;
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

    public ArrayList<Contact> getContacts() {
        return contacts;
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

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}

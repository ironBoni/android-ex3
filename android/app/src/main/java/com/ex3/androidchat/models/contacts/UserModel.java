package com.ex3.androidchat.models.contacts;

import com.ex3.androidchat.models.Chat;

import java.util.List;

public class UserModel {
    String username, nickname, password, profileImage, server;
    List<Chat> chats;
    List<ContactModel> contacts;

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
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

    public List<Chat> getChats() {
        return chats;
    }

    public List<ContactModel> getContacts() {
        return contacts;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public void setContacts(List<ContactModel> contacts) {
        this.contacts = contacts;
    }
}

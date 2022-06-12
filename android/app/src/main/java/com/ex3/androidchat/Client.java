package com.ex3.androidchat;

import com.ex3.androidchat.models.Contact;

import java.util.ArrayList;

public class Client {
    public static String firebaseToken = "";
    private static String dataServer = "http://10.0.2.2:5186/api/";
    private static String token = "";
    private static String userId = "";
    private static String friendNickname = "";
    private static String friendServer = "";
    private static String friendImage = "";
    public static MainActivity mainActivity;
    public static ConversationActivity conversationActivity;
    public static ArrayList<Contact> contacts;

    public static String getFriendId() {
        return friendId;
    }

    public static void setFriendId(String friendId) {
        Client.friendId = friendId;
    }

    private static String friendId = "";
    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String username) {
        Client.userId = username;
    }

    public static void setToken(String t) {
        token = t;
    }

    public static String getMyServer() {
        return dataServer;
    }

    public static String getToken() {
        return token;
    }

    public static String getFriendNickname() {
        return friendNickname;
    }

    public static void setFriendNickname(String friendNickname) {
        Client.friendNickname = friendNickname;
    }

    public static String getFriendServer() {
        return friendServer;
    }

    public static void setFriendServer(String friendServer) {
        Client.friendServer = friendServer;
    }

    public static String getFriendImage() {
        return friendImage;
    }

    public static void setFriendImage(String friendImage) {
        Client.friendImage = friendImage;
    }

    public static void setMyServer(String myServer) {
        dataServer = myServer;
    }
}

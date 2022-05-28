package com.ex3.androidchat.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class Message {
    int id;
    String type, text, senderUsername, fileName;
    String writtenIn;
    boolean sent;

    public Message(int id, String text, String senderUsername, String writtenIn) {
        this.id = id;
        this.text = text;
        this.senderUsername = senderUsername;
        this.writtenIn = writtenIn;
    }

    public Message(int id, String type, String text, String senderUsername, String writtenIn, String fileName, boolean sent) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.senderUsername = senderUsername;
        this.writtenIn = writtenIn;
        this.fileName = fileName;
        this.sent = sent;
    }


    public Message(int id, String text, String senderUsername) {
        this(id, "text", text, senderUsername, "30.05.2022 16:26");
    }

    public Message(int id, String type, String text, String senderUsername, String writtenIn, String fileName) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.senderUsername = senderUsername;
        this.writtenIn = writtenIn;
        this.fileName = fileName;
    }

    public Message(int id, String type, String text, String senderUsername, String writtenIn) {
        this(id, type, text, senderUsername, writtenIn, "");
    }


    public Message(int id, String type, String text, String senderUsername, String writtenIn, boolean sent)
    {
        this(id, type, text, senderUsername, writtenIn, "");
        this.sent = sent; 
    }
    

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getWrittenIn() {
        return writtenIn;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isSent() {
        return sent;
    }
}

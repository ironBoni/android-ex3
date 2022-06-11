package com.ex3.androidchat.models.contacts;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageResponse {
    public int id;
    public String content, type, senderUsername, fileName;
    public String created;
    public boolean sent;

    public MessageResponse() {
    }

    public MessageResponse(int id, String content, String type, String senderUsername, String fileName, String created, boolean sent) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.senderUsername = senderUsername;
        this.fileName = fileName;
        this.created = created;
        this.sent = sent;
    }
    public MessageResponse(int id, String text, String senderUsername) {
        this.content = text;
        this.id = id;
        this.senderUsername = senderUsername;
        sent = true;
        type = "text";
        java.util.Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        created = formatter.format(date);
        fileName = "";
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCreated() {
        return created;
    }

    public boolean isSent() {
        return sent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}

package com.ex3.androidchat.models.contacts;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class MessageResponse {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String senderUsername;
    public String content, type, fileName;
    public String created;
    public boolean sent;
    public String user;
    public String createdDateStr;
    public int chatId;

    public String getCreatedDateStr() {
        return createdDateStr;
    }

    public void setCreatedDateStr(String createDateStr) {
        this.createdDateStr = createDateStr;
    }

    @Ignore
    public MessageResponse() {
    }
    public MessageResponse(String senderUsername,String content,String user,int chatId) {
        this.senderUsername = senderUsername;
        this.user=user;
        this.content = content;
        this.created = (new Date()).toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.createdDateStr = formatter.format(new Date());
        fileName = "";
        sent = true;
        type = "text";
        this.chatId = chatId;
    }
    @Ignore
    public MessageResponse(int id, String content, String type, String senderUsername, String fileName, String createdDateStr, boolean sent) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.senderUsername = senderUsername;
        this.fileName = fileName;
        this.createdDateStr = createdDateStr;
        this.sent = sent;
    }
    @Ignore
    public MessageResponse(int id, String text, String senderUsername) {
        this.content = text;
        this.id = id;
        this.senderUsername = senderUsername;
        sent = true;
        type = "text";
        this.created = (new Date()).toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.createdDateStr = formatter.format(new Date());
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
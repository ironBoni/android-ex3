package com.ex3.androidchat.models.contacts;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ex3.androidchat.Client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class MessageResponse {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String senderUsername;
    public String content, type, fileName;
    public String created;
    public boolean sent;
    public String user;

    @Ignore
    public MessageResponse() {
    }
    public MessageResponse(String senderUsername,String content,String user ) {
        this.senderUsername = senderUsername;
        this.user=user;
        this.content = content;
        java.util.Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        created = formatter.format(date);
        fileName = "";
        sent = true;
        type = "text";
    }
    @Ignore
    public MessageResponse(int id, String content, String type, String senderUsername, String fileName, String created, boolean sent) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.senderUsername = senderUsername;
        this.fileName = fileName;
        this.created = created;
        this.sent = sent;
    }
    @Ignore
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

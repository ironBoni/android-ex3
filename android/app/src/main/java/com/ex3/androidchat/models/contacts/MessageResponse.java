package com.ex3.androidchat.models.contacts;

    import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageResponse {
    public int id;
    public String content, type, senderUsername, fileName;
    public String createdDateStr;
    public boolean sent;

    public MessageResponse() {
    }

    public MessageResponse(int id, String content, String type, String senderUsername, String fileName, String createDateStr, boolean sent) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.senderUsername = senderUsername;
        this.fileName = fileName;
        this.createdDateStr = createDateStr;
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
        createdDateStr = formatter.format(date);
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

    public String getCreatedDateStr() {
        return createdDateStr;
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

    public void setCreatedDateStr(String createdDateStr) {
        this.createdDateStr = createdDateStr;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}

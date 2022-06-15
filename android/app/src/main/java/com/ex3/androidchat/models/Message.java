package com.ex3.androidchat.models;



import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    public int id;
    public String type, text, senderUsername, fileName;
    public String writtenIn;
    public boolean sent;

    public Message(int id, String text, String senderUsername, String writtenIn) {
        this.id = id;
        this.text = text;
        this.senderUsername = senderUsername;
        this.writtenIn = writtenIn;
    }

    public Message() {
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
        this.text = text;
        this.id = id;
        this.senderUsername = senderUsername;
        sent = true;
        type = "text";
        java.util.Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        writtenIn = formatter.format(date);
        fileName = "";
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

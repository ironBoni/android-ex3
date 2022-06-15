package com.ex3.androidchat.models;

import static java.util.UUID.randomUUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ex3.androidchat.Client;

@Entity
public class Contact {
    @PrimaryKey
    @NonNull
    public String id;
    public String contactId;
    public String name, server, last;
    @ColumnInfo(name = "profileImage")
    public String profileImage;
    public String lastdate;
    public String lastdateStr;
    public String ofUser;

    @Ignore
    public Contact() { }
    @Ignore
    public Contact(String id, String contactId, String name, String server, String last, String profileImage, String lastdate,
                   String lastdateStr, String ofUser) {
        this.id = id;
        this.contactId = contactId;
        this.name = name;
        this.server = server;
        this.last = last;
        this.profileImage = profileImage;
        this.lastdate = lastdate;
        this.lastdateStr = lastdateStr;
        this.ofUser = ofUser;
    }

    public Contact(String contactId, String name, String server, String last, String profileImage, String lastdate, String lastdateStr, String ofUser) {
        this.id = randomUUID().toString();
        this.contactId = contactId;
        this.name = name;
        this.server = server;
        this.last = last;
        this.profileImage = profileImage;
        this.lastdate = lastdate;
        this.lastdateStr = lastdateStr;
        this.ofUser = ofUser;
    }

    public String getOfUser() {
        return ofUser;
    }

    public void setOfUser(String ofUser) {
        this.ofUser = ofUser;
    }

    @Ignore
    public Contact(String id, String contactId, String name, String server) {
        this.id = id;
        this.contactId = contactId;
        this.name = name;
        this.server = server;
        this.last = null;
        this.lastdateStr =null;
        this.lastdate = null;
        this.profileImage = "";
    }

    public String getId() {
        return id;
    }
    public String getContactId() { return contactId; }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public String getLast() {
        return last;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getLastdate() {
        return lastdate;
    }

    public String getLastdateStr() {
        return lastdateStr;
    }

    public void setLastdateStr(String lastdateStr) {
        this.lastdateStr = lastdateStr;
    }
}

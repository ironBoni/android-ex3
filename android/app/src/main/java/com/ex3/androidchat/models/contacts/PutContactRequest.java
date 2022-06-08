package com.ex3.androidchat.models.contacts;

public class PutContactRequest {
    String name, server;

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServer(String server) {
        this.server = server;
    }
}

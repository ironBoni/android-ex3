package com.ex3.androidchat.models.contacts;

public class PutContactRequest {
    public String name, server;

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public PutContactRequest() {
    }

    public PutContactRequest(String name, String server) {
        this.name = name;
        this.server = server;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServer(String server) {
        this.server = server;
    }
}

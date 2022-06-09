package com.ex3.androidchat.models.contacts;

public class ContactRequest {
    String id, name, server;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContactRequest() {
    }

    public ContactRequest(String id, String name, String server) {
        this.id = id;
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

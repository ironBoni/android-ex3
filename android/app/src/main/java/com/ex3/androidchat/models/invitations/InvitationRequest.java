package com.ex3.androidchat.models.invitations;

public class InvitationRequest {
    String from, to, server;

    public InvitationRequest(String from, String to, String server) {
        this.from = from;
        this.to = to;
        this.server = server;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getServer() {
        return server;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setServer(String server) {
        this.server = server;
    }
}

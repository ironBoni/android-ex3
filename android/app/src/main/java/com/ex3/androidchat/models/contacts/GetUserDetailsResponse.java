package com.ex3.androidchat.models.contacts;

public class GetUserDetailsResponse {
    public String server, name, profileImage;

    public String getServer() {
        return server;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public GetUserDetailsResponse() {
    }

    public GetUserDetailsResponse(String server, String name, String profileImage) {
        this.server = server;
        this.name = name;
        this.profileImage = profileImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

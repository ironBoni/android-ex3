package com.ex3.androidchat.models.login;

public class TokenResponse {
    public String token;

    public String getToken() {
        return token;
    }

    public TokenResponse() {
    }

    public TokenResponse(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

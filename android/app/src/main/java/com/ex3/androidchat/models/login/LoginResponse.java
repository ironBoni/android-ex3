package com.ex3.androidchat.models.login;

public class LoginResponse {
    public String response, correctPass;
    public boolean isCorrectInput;
    public TokenResponse token;

    public LoginResponse() {
    }

    public LoginResponse(String response, boolean isCorrectInput, TokenResponse token, String correctPass) {
        this.response = response;
        this.isCorrectInput = isCorrectInput;
        this.token = token;
        this.correctPass = correctPass;
    }

    public String getResponse() {
        return response;
    }

    public boolean isCorrectInput() {
        return isCorrectInput;
    }

    public TokenResponse getToken() {
        return token;
    }

    public String getCorrectPass() {
        return correctPass;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setCorrectInput(boolean correctInput) {
        isCorrectInput = correctInput;
    }

    public void setToken(TokenResponse token) {
        this.token = token;
    }

    public void setCorrectPass(String correctPass) {
        this.correctPass = correctPass;
    }
}

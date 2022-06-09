package com.ex3.androidchat.models.contacts;

public class PutMessageRequest {
    public String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PutMessageRequest() {
    }

    public PutMessageRequest(String content) {
        this.content = content;
    }
}

package com.ex3.androidchat.models.contacts;

public class SendMessageRequest {
    public String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SendMessageRequest() {
    }

    public SendMessageRequest(String content) {
        this.content = content;
    }
}

package com.ex3.androidchat;

public class Response {
    private byte[] res;
    private int status;

    public Response(byte[] response, int status) {
        this.res = response;
        this.status = status;
    }

    public byte[] getResponse() {
        return this.res;
    }

    public int getStatus() {
        return this.status;
    }
}

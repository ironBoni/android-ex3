package com.ex3.androidchat.models;

public class Utils {
    public static String getFullServerUrl(String url)
    {
        if (!url.endsWith("/"))
            url = url + "/";
        if (!url.startsWith("http://"))
            url = "http://" + url;
        return url;
    }

    public static String getAndroidServer(String friendServer) {
        return  friendServer.replace("localhost", "10.0.2.2").replace("127.0.0.1", "10.0.2.2");
    }
}

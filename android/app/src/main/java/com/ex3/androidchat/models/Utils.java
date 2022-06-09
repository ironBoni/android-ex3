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
}

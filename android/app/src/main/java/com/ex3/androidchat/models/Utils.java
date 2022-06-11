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

    public static String getHour(String dateAndHour) {
        String[] parts = dateAndHour.split(" ");
        if(parts.length <= 1)
            return dateAndHour;
        return parts[1];
    }

    public static String getAndroidServer(String friendServer) {
        friendServer = getFullServerUrl(friendServer);
        if(!friendServer.endsWith("api/"))
            friendServer = friendServer + "api/";
        return  friendServer.replace("localhost", "10.0.2.2").replace("127.0.0.1", "10.0.2.2");
    }

    public static String getLastWord(String sentence) {
        String[] parts = sentence.split(" ");
        if(parts.length <= 1)
            return sentence;
        return parts[parts.length - 1];
    }
}

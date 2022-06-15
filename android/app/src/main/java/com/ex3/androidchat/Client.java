package com.ex3.androidchat;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class Client {
    public static String firebaseToken = "";
    public static NotificationsService notifyService;
    private static String dataServer = "http://10.0.2.2:5186/api/";
    private static String token = "";
    private static String userId = "";
    private static String friendNickname = "";
    private static String friendServer = "";
    private static String friendImage = "";
    private static boolean isNightModeOn = false;
    public static MainActivity mainActivity;
    public static ConversationActivity conversationActivity;
    public static ArrayList<Message> messages;
    public static ArrayList<Contact> contacts;
    public static boolean isIsNightModeOn() {
        return isNightModeOn;
    }

    public static void setIsNightModeOn(boolean isNightModeOn) {
        Client.isNightModeOn = isNightModeOn;
    }

    public static String getFriendId() {
        return friendId;
    }

    public static void setFriendId(String friendId) {
        Client.friendId = friendId;
    }

    private static String friendId = "";
    public static String defaultImage = "https://www.stignatius.co.uk/wp-content/uploads/2020/10/default-user-icon.jpg";
    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String username) {
        Client.userId = username;
    }

    public static void setToken(String t) {
        token = t;
    }

    public static String getMyServer() {
        return dataServer;
    }

    public static String getToken() {
        return token;
    }

    public static String getFriendNickname() {
        return friendNickname;
    }

    public static void setFriendNickname(String friendNickname) {
        Client.friendNickname = friendNickname;
    }

    public static String getFriendServer() {
        return friendServer;
    }

    public static void setFriendServer(String friendServer) {
        Client.friendServer = friendServer;
    }

    public static String getFriendImage() {
        return friendImage;
    }

    public static void setFriendImage(String friendImage) {
        Client.friendImage = friendImage;
    }

    public static Response sendPost(String resUrl, Map<String, Object> bodyMap) {
        return doPost(resUrl, bodyMap);
    }

    public static Bitmap image;

    @Nullable
    private static Response doPost(String resUrl, Map<String, Object> bodyMap) {
        try {
            URL url = new URL(dataServer + resUrl);

            StringBuilder postData = new StringBuilder();
            postData.append("{");
            int count = 0;
            int max = bodyMap.keySet().size() - 1;
            for (Map.Entry<String, Object> param : bodyMap.entrySet()) {
                postData.append("\"");
                postData.append(param.getKey());
                postData.append("\":");
                postData.append("\"");
                postData.append(param.getValue());
                postData.append("\"");
                if (count < max)
                    postData.append(", ");
                count++;
            }
            postData.append("}");
            System.out.println(postData.toString());
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "*/*");
            if(!token.equals(""))
                conn.setRequestProperty("Authorization", "Bearer " + token);
            String payload = postData.toString();
            byte[] out = payload.getBytes(StandardCharsets.UTF_8);
            conn.setDoOutput(true);
            OutputStream stream = conn.getOutputStream();
            stream.write(out);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0; )
                sb.append((char) c);
            byte[] response = sb.toString().getBytes();
            int statusCode = conn.getResponseCode();
            conn.disconnect();
            return new Response(response, statusCode);
        } catch (Exception e) {
            Log.d("SendToServer", e.toString());
        }
        return null;
    }

    public static Response sendGet(String apiUrl) {
        return doGet(apiUrl);
    }

    @Nullable
    private static Response doGet(String apiUrl) {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(dataServer + apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if(!token.equals(""))
                conn.setRequestProperty("Authorization", "Bearer " + token);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            byte[] response = result.toString().getBytes();
            int statusCode = conn.getResponseCode();

            conn.disconnect();
            return new Response(response, statusCode);
        } catch(Exception e) {
            Log.d("Exception", e.toString());
            return null;
        }
    }

    public static void setMyServer(String myServer) {
        dataServer = myServer;
    }
}

package com.ex3.androidchat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Client {
    private static String dataServer = "http://localhost:5186/api/";
    private static String token = "";

    public static void sendPost(String resUrl, Map<String, Object> bodyMap) {
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
            String response = sb.toString();

            conn.disconnect();
        } catch (Exception e) {
            Log.d("SendToServer", e.toString());
        }
    }
}

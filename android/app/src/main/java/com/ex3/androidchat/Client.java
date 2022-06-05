package com.ex3.androidchat;

import android.util.Log;

import androidx.annotation.Nullable;

import com.ex3.androidchat.Response;

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
    private static String user = "";
    public static String defaultImage = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/bAEMABAMDBAMDBAQEBAUFBAUHCwcHBgYHDgoKCAsQDhEREA4QDxIUGhYSExgTDxAWHxcYGxsdHR0RFiAiHxwiGhwdHP/AAAsIAFoAWgEBEQD/xAAbAAEAAgMBAQAAAAAAAAAAAAAABwgCBQYBBP/EADsQAAEDAgMEBgcFCQAAAAAAAAEAAgMEBQYHERIhMUEIE1FhcYEUFiJScpGhFUJTkqIXJDIzYoOTseH/2gAIAQEAAD8AukiIiIiIiIiIuTxLmVhrCkj4a+4NdVt401O3rZB4gbm+ZC4qTpFWJshDLRc3s94ujaflqt/Ys6sJ3uRsL6uW3zOOgbWs2Gk/GCW/MhSC1zXta5rg5rhqHA6gjtBXqIiIig7ODNeehqZ8O2KcxTM9msq4z7TD+Gw8j2nyHNQGSSSSSSTqSeZXiKRcs80qzBtVHRVskk9hkdo+InU0+v32d3a3gfFWmhmjqIY5oZGyRSND2PadQ5pGoI7iFmiIi0eMb76s4Wut1GhkpYC6MHnIfZb+ohUvkkfNI+SR5fI9xc57uLiTqSfErFERWYyDxC+64Tntszy6W1ShjCePVP1LR5EOHyUrIiIo7zw2/wBnNw2ddOug2vDrB/xVVRERTd0cNv7QxFp/L6iHXx23afTVWARERaPGNi9ZsLXW1DTrKqAtjJ5SDe39QCpfJG+GR8crCyRji1zHDQtIOhB81iiIrM5CYdfasJz3KZuzLdZRIwHj1TNQ0+Z2j8lKqIiIoPzfyonr6ifEVigMs7/arKRg9p5/EYOZ7R5jmoCIIJBBBB0IPIrxFIuWeVtXjKrjra2N8Fhjdq+QjQ1Gn3Gd3a7gPFWmhhjp4Y4YWNjijaGMY0aBrQNAB3aLNERFoMU40suDqUT3WrEb3jWOBg2pZfhb2d50HetNgzNaw4xkNPFI6ir9ohtLVOAdIORa4bneHEL7MS5a4axVI+a4W5rat3Gpp3dVKfEjcfMFcVJ0dbE6Qll3ubGe7pG767K39iyWwlZJGzPpJa+Zu8OrX7bQfgADfmCujxPjCy4LoeuuVSyEhukVNGAZJNOAawcu/cAtXg3M+w40IgpZnU1w019DqdGvPwkbneW/uXZoiLkMxMd0+BbIaktbLcKjVlLTk/xO5uP9LefbuHNVNu13rb5cJ6+4VD6irnOr5Hn6AcgOQHBfEN3+12djzWxZYI2xU92knp2bhFVtEzQOwF28fNdOzpDYlazR1BanO97q5B9Ntai6Z24wubHMZWw0LHbv3OENd+Y6lcDU1U9bO+oqZpJ55Dq6SV5c53iTvWMUr4JGSRvcyRhDmvadC0jgQeRVmcoczXYspjabrIPtqnZtNkO70qMcXfEOfbx7VKSLxzmsaXOcGtaNS48AO1U8zDxa/GWKKuvDj6Gw9TSsP3Ymk6HxO9x8VyqIiIi+20XWqsdzpbjRSbFVSyCSN3eOR7jwPcVc/D16p8R2ShutLuhq4hIG+6ebT4EEeS2S4jNy9useAbrJG7Znqg2kjI4gyHQn8u0qkIiIiIisR0d726psl0tEjtTRTNnjB5Mk4j8zf1KZlEfSHJGELcNdxrxr/jeq2oiIiIimLo6E+s14Gu40I3f3Gqxi/9k=";
    public static String getUser() {
        return user;
    }

    public static void setUser(String username) {
        Client.user = username;
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

    public static Response sendPost(String resUrl, Map<String, Object> bodyMap) {
        return doPost(resUrl, bodyMap);
    }

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
}

package com.ex3.androidchat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.room.TypeConverter;

import com.ex3.androidchat.models.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

public class DataConverter implements Serializable {
    public static String BitmapToBase64(Bitmap source) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageBytesStr = Base64.encodeToString(b, Base64.DEFAULT);
        return imageBytesStr;
    }

    public static Bitmap Base64ToBitmap(String source) {
        byte[] encodeByte = Base64.decode(source, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

    }


    public static Bitmap UrlToBitmap(String urlString) throws IOException {
        try {

            //uncomment below line in image name have spaces.
            //src = src.replaceAll(" ", "%20");

            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            Log.d("vk21", e.toString());
            return null;
        }
    }

    @TypeConverter
    public static ArrayList<Contact> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Contact> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
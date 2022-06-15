package com.ex3.androidchat.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.ex3.androidchat.DataConverter;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class GetByAsyncTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;

    public GetByAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urlStrings) {
        try {
            URL url = new URL(urlStrings[0]);
            // here means it is URL, then do nothing
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // otherwise it's bitmap, then update the image.
            String imageBytesStr = urlStrings[0];

            Bitmap bitmap;
            try {
                byte [] encodeByte= Base64.decode(imageBytesStr,Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
            } catch(Exception ex) {
                ex.getMessage();
                return null;
            }
        }

        try {
            String url = urlStrings[0];
            Bitmap image = null;
            InputStream input = new java.net.URL(url).openStream();
            image = BitmapFactory.decodeStream(input);
            return image;
        } catch (Exception ex) {
            Log.e("error in getting image", ex.getMessage());
            return null;
        }
    }

    protected void onPostExecute(Bitmap imageFromServer) {
        imageView.setImageBitmap(imageFromServer);
    }
}

package com.ex3.androidchat.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class GetByAsyncTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;

    public GetByAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urlStrings) {
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

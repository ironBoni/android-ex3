package com.ex3.androidchat.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.ex3.androidchat.AndroidChat;
import com.ex3.androidchat.Client;
import com.ex3.androidchat.DataConverter;
import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Observer;

public class AsyncTaskDao extends AsyncTask<String, Void, String> {
    ContactDao contactDao;
    String id;
    public AsyncTaskDao(ContactDao contactDao, String idOfContact) {

        this.contactDao=contactDao;
        this.id = idOfContact;
    }

    @Override
    protected String doInBackground(String... urlStrings) {
        Bitmap bitmap=null;
        try {
            URL url = new URL(urlStrings[0]);
            // here means it is URL, then do nothing
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // otherwise it's bitmap, then update the image.
            String imageBytesStr = urlStrings[0];


            try {
                byte [] encodeByte= Base64.decode(imageBytesStr,Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            } catch(Exception ex) {
                ex.getMessage();
                return null;
            }
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();
            String imageBytesStr = Base64.encodeToString(b, Base64.DEFAULT);
            return imageBytesStr;

        } catch (Exception ex) {
            Log.e("error in getting image", ex.getMessage());
            return null;
        }
    }

    protected void onPostExecute(String imageBytesStr) {
        if(imageBytesStr == null) {
            String uri = "@drawable/default_avatar";
            if(AndroidChat.context == null) return;
            tryUpdatingTheRoomByImage(uri);
        }
        else {
            contactDao.update(imageBytesStr, id);
        }
    }

    private void tryUpdatingTheRoomByImage(String uri) {
        try {
            Client.mainActivity.runOnUiThread(() -> {
                int imageResource = AndroidChat.context.getResources().getIdentifier(uri, null, AndroidChat.context.getPackageName());
                Drawable res = AndroidChat.context.getResources().getDrawable(imageResource);
                Bitmap bitmap = ((BitmapDrawable) res).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] b = byteArrayOutputStream.toByteArray();

                String imgStr = Base64.encodeToString(b, Base64.DEFAULT);
                contactDao.update(imgStr, id);
            });
        } catch(Exception e) {
            Log.e("error", e.toString());
        }
    }
}

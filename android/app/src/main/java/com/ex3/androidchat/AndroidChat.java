package com.ex3.androidchat;

import android.app.Application;
import android.content.Context;

public class AndroidChat extends Application {
    public static Context context;

    public void OnCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}

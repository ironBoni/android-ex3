package com.ex3.androidchat.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.Message;
@Database(entities = {Contact.class}, version = 1, exportSchema = false)
//@Database(entities = {Contact.class, Message.class, Chat.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    private static  AppDB contactDB = null;

    public abstract ContactDao contactDao();
    //    public abstract ChatDao chatDao();
    public static synchronized AppDB getContactDBInstance(Context context){
        if (contactDB == null){
            contactDB = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDB.class, "ContactDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return contactDB;
    }
}
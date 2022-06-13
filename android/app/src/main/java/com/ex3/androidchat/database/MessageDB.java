package com.ex3.androidchat.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.Message;

@Database(entities = {Message.class}, version = 1, exportSchema = false)
public abstract class MessageDB extends RoomDatabase {
    private static  MessageDB messageDB = null;

    public abstract MessageDao messageDao();
    public static synchronized MessageDB getMessageDBInstance(Context context){
        if (messageDB == null){
            messageDB = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MessageDB.class, "MessageDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return messageDB;
    }

}

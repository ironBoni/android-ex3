package com.ex3.androidchat.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ex3.androidchat.models.Message;
import com.ex3.androidchat.models.contacts.MessageResponse;

import java.util.HashMap;
import java.util.List;


@Database(entities = {MessageResponse.class}, version = 4 , exportSchema = false)
public abstract class MessageDB extends RoomDatabase {
//    private static HashMap<String, MessageDB> messageDB = new HashMap<>();
    private static MessageDB messageDB = null;
    public abstract MessageDao messageDao();
    public static synchronized MessageDB insert( Context context){
        if (messageDB == null) {
            messageDB = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MessageDB.class, "messageDB")
                    .allowMainThreadQueries()
                    .build();
        }
        return messageDB;

    }
//    public static synchronized HashMap<String, MessageDB> getMessageDBInstance(Context context, String friendID) {
//        if (messageDB.get(friendID) == null) {
//            messageDB.put(friendID, (Room.databaseBuilder(
//                            context.getApplicationContext(),
//                            MessageDB.class, "MessageDB")
//                    .allowMainThreadQueries()
//                    .build()));
//        }
//        return messageDB;
//    }
}

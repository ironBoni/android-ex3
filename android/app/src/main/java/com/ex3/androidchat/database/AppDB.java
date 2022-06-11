package com.ex3.androidchat.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ex3.androidchat.models.Contact;

@Database(entities = {Contact.class,}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
}

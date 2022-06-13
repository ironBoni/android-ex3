package com.ex3.androidchat.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;

import java.util.List;

//@Dao
//public interface ChatDao {
//    @Query("Select * from chat")
//    List<Chat> getChats();
//    @Query("SELECT * FROM chat WHERE id = :id")
//    Chat get(int id);
//    @Insert
//    void insert(Chat chat);
//    @Update
//    void update(Chat chat);
//}

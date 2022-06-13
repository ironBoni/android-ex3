package com.ex3.androidchat.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("Select * from message")
    List<Message> getList();
    @Query("SELECT * FROM chat WHERE id = :id")
    Message get(int id);
    @Insert
    void insert(Message message);
    @Update
    void update(Message message);
}

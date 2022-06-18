package com.ex3.androidchat.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Message;
import com.ex3.androidchat.models.contacts.MessageResponse;

import java.util.List;



@Dao
public interface MessageDao {
    @Query("Select * from messageresponse")
    List<MessageResponse > index();
    @Query("SELECT * FROM messageresponse WHERE id = :id")
    MessageResponse get(int id);
    @Query("SELECT * FROM messageresponse WHERE senderUsername=:username")
    List<MessageResponse> isUserExits(String username);
    @Query("SELECT * FROM messageresponse WHERE chatId=:chatId")
    List<MessageResponse> getMessagesByChatId(int chatId);
    @Insert
    void insert(MessageResponse message);
    @Insert
    void insertList(List<MessageResponse> messageResponseList);
    @Update
    void update(MessageResponse message);
    @Query("DELETE FROM messageresponse")
    void deleteAll();
}

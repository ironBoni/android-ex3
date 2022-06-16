package com.ex3.androidchat.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ex3.androidchat.models.Contact;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> index();

    @Query("SELECT * FROM contact WHERE id = :id")
    Contact get(int id);
    @Query("UPDATE contact SET profileImage=:img WHERE id =:id ")
    void update(String img, String id);
    @Query("SELECT profileImage FROM contact WHERE id=:id")
    String getURL(String id);
    @Query("SELECT id FROM contact ")
    List<String> getAllIds();
    @Query("UPDATE contact SET last=:last WHERE contactId =:contactId ")
    void updateLast(String last, String contactId);
    @Insert
    void insert(Contact contacts);

    @Insert
    void insertList(List<Contact> contacts);

    @Update
    void update(Contact contacts);

    @Delete
    void delete(Contact contacts);

    @Delete
    void deleteList(List<Contact> contacts);

    @Query("DELETE FROM contact")
    void deleteAll();
}

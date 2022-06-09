package com.ex3.androidchat.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ex3.androidchat.models.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> index();

    @Query("SELECT * FROM contact WHERE id = :id")
    Contact get(int id);

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
}

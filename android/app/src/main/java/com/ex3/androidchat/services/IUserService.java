package com.ex3.androidchat.services;

import com.ex3.androidchat.models.User;

import java.util.ArrayList;

public interface IUserService extends IContactService {
    ArrayList<User> GetAll();
    User GetById(String id);
    boolean Create(User user);
    boolean Update(User user);
    boolean Delete(User user);
    String GetFullServerUrl(String url);
}

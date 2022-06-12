package com.ex3.androidchat.services;

import com.ex3.androidchat.models.User;

import java.util.ArrayList;

public interface IUserService extends IContactService {
    ArrayList<User> getAll();
    User getById(String id);
    boolean create(User user);
    boolean update(User user);
    boolean delete(String userId);
    String getFullServerUrl(String url);
}

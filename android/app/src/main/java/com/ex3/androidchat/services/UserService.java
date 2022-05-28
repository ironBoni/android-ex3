package com.ex3.androidchat.services;

import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.User;

import java.util.ArrayList;
import java.util.Arrays;

public class UserService implements  IUserService {
    private static ArrayList<User> users = new ArrayList<>(Arrays.asList(
            new User("noam", "Noam Cohen", "Np1234", "/profile/noam.jpg"),
            new User("hadar", "Hadar Pinto", "Np1234", "/profile/hadar.jpg"),
            new User("dvir", "Dvir Pollak", "Np1234", "/profile/dvir.jpg"),
            new User("ron", "Ron Solomon", "Np1234", "/profile/ron.jpg"),
            new User("dan", "Dan Cohen", "Np1234", "/profile/dan.jpg"),
            new User("idan", "Idan Ben Ari", "Np1234", "/profile/idan.jpg"),
            new User("shlomo", "Shlomo Levin", "Np1234", "/profile/shlomo.png"),
            new User("yaniv", "Yaniv Hoffman", "Np1234", "/profile/yaniv.png"),
            new User("oren", "Oren Orbach", "Np1234", "/profile/oren.webp"),
            new User("yuval", "Yuval Baruchi", "Np1234", "/profile/yuval.png"),
            new User("ran", "Ran Levi", "Np1234", "/profile/ran.webp")
            ));

    private static IChatService chatsService = new ChatService();

    @Override
    public ArrayList<Contact> GetContacts(String username) {
        return null;
    }

    @Override
    public boolean AddContact(String id, String name, String server) {
        return false;
    }

    @Override
    public boolean AcceptInvitation(String from, String server, String to) {
        return false;
    }

    @Override
    public boolean RemoveContact(String username) {
        return false;
    }

    @Override
    public ArrayList<User> GetAll() {
        return null;
    }

    @Override
    public User GetById(String id) {
        return null;
    }

    @Override
    public boolean Create(User user) {
        return false;
    }

    @Override
    public boolean Update(User user) {
        return false;
    }

    @Override
    public boolean Delete(User user) {
        return false;
    }

    @Override
    public String GetFullServerUrl(String url) {
        return null;
    }
}

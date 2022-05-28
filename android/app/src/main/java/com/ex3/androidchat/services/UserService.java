package com.ex3.androidchat.services;

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
        User user = getById(username);
        if(user == null) return null;
        return user.getContacts();
    }

    @Override
    public boolean AddContact(String id, String name, String server) {
        ArrayList<Contact> contacts = GetContacts(id);
        if(contacts == null) return false;
        return contacts.add(new Contact(id, name, server));
    }

    @Override
    public boolean RemoveContact(String username) {
        ArrayList<Contact> contacts = GetContacts(username);
        if(contacts == null) return false;
        Contact contactToRemove = null;
        
        for(Contact contact : contacts) {
            if(contact.getId() == username) {
                contactToRemove = contact;        
            }
        }
        
        if(contactToRemove == null)
            return false;
        return contacts.remove(contactToRemove);
    }

    @Override
    public ArrayList<User> getAll() {
        return users;
    }

    @Override
    public User getById(String id) {
        for(User user : users) {
            if(user.getId().equals(id))
                return user;
        }
        return null;
    }

    @Override
    public boolean create(User user) {
        return users.add(user);
    }

    @Override
    public boolean update(User user) {
        delete(user.getId());
        return create(user);
    }

    @Override
    public boolean delete(String userId) {
        for (User user : users) {
            if (user.getId() == userId)
                users.remove(user);
            return true;
        }
        return false;
    }

    @Override
    public String getFullServerUrl(String url) {
        return url;
    }

    @Override
    public boolean isLoginOk(String username, String password) {
        User user = getById(username);
        if(user == null) return false;
        return user.getPassword().equals(password);
    }
}

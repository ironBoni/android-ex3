package com.ex3.androidchat.services;

import com.ex3.androidchat.Client;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.User;
import com.ex3.androidchat.models.contacts.UserModel;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService implements  IUserService {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    private static ArrayList<User> users = new ArrayList<>();

    public static void setUsers(ArrayList<UserModel> list) {
        users.clear();
        for(UserModel model : list) {
            User user = new User(model.getId(), model.getPassword(), null, model.getProfileImage(), model.getName(),
                    model.getServer(), null);
            users.add(user);
        }
    }

    public static ArrayList<User> getUsers() {
        return users;
    }
    private static IChatService chatsService = new ChatService();

    public String getOtherUserIdInChat(Chat chat, String firstId) {
        for(String participant: chat.getParticipants()) {
            if(participant != firstId)
                return participant;
        }
        return firstId;
    }

    public boolean isContactOf(User user, String secondId) {
        User otherUser = getById(secondId);
        for(Contact contact : user.getContacts()) {
            if(contact.getContactId() == secondId)
                return true;
        }
        return false;
    }

    @Override
    public ArrayList<Contact> getContacts(String username) {
        User user = getById(username);
        if(user == null) return null;
        return user.getContacts();
    }

    @Override
    public boolean addContact(String addTo, String contactId, String name, String server, String last, String lastDate, String profileImage) {
        User addToUser = getById(addTo);
        if(addToUser == null) return false;
        return addToUser.getContacts().add(new Contact(contactId, name, server, last, profileImage, lastDate));
    }

    @Override
    public boolean removeContact(String username) {
        ArrayList<Contact> contacts = getContacts(username);
        if(contacts == null) return false;
        Contact contactToRemove = null;
        
        for(Contact contact : contacts) {
            if(contact.getContactId().equals(username)) {
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


    public String getFullServerUrl(String url) {
        if (!url.endsWith("/"))
            url = url + "/";
        if (!url.startsWith("http://"))
            url = "http://" + url;
        return url;
    }
}

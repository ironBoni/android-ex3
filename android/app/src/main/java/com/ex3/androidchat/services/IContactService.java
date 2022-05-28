package com.ex3.androidchat.services;

import com.ex3.androidchat.models.Contact;

import java.util.ArrayList;

public interface IContactService {
    ArrayList<Contact> getContacts(String username);
    boolean addContact(String id, String name, String server);
    boolean removeContact(String username);
}

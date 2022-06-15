package com.ex3.androidchat.services;

import com.ex3.androidchat.models.Contact;

import java.util.ArrayList;

public interface IContactService {
    ArrayList<Contact> getContacts(String username);
    boolean addContact(String addTo, String id, String name, String server, String last, String lastDate, String profileImage, String lastdateStr);
    boolean removeContact(String username);
}

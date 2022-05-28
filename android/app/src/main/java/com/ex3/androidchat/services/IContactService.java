package com.ex3.androidchat.services;

import com.ex3.androidchat.models.Contact;

import java.util.ArrayList;

public interface IContactService {
    ArrayList<Contact> GetContacts(String username);
    boolean AddContact(String id, String name, String server);
    boolean AcceptInvitation(String from, String server, String to);
    boolean RemoveContact(String username);
}

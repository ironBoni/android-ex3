package com.ex3.androidchat.models;

import java.util.ArrayList;

public class Chat {
    private static int staticId = 16;
    private int id;
    ArrayList<String> participants;
    ArrayList<Message> messages;

    public Chat(int id, ArrayList<String> participants, ArrayList<Message> messages)
    {
        this.id = id;
        this.participants = participants;
        this.messages = messages;
    }

    public Chat(ArrayList<String> participants, ArrayList<Message> messages)
    {
        this.id = staticId;
        staticId++;
        this.participants = participants;
        this.messages = messages;
    }

    public Chat(ArrayList<String> participants)
    {
        this(participants, new ArrayList<Message>());
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
}
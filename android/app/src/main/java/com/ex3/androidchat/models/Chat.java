package com.ex3.androidchat.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private static int staticId = 16;

    public int id;
    public ArrayList<String> participants;
    public ArrayList<Message> messages;

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public Chat() {
    }

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

    public List<Message> getMessages() {
        return messages;
    }
    public void addMessage(String message, String senderId) {
        int maxId = 0;
        for(Message m : messages) {
            if(m.getId() > maxId) {
                maxId = m.getId();
            }
        }
        messages.add(new Message(maxId + 1, message, senderId));
    }

}

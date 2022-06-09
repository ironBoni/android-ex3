package com.ex3.androidchat.models;

import com.ex3.androidchat.models.contacts.MessageResponse;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private static int staticId = 16;
    public int id;
    public ArrayList<String> participants;
    public List<MessageResponse> messages;
    public Chat() {
    }

    public Chat(int id, ArrayList<String> participants, ArrayList<MessageResponse> messages)
    {
        this.id = id;
        this.participants = participants;
        this.messages = messages;
    }

    public Chat(ArrayList<String> participants, ArrayList<MessageResponse> messages)
    {
        this.id = staticId;
        staticId++;
        this.participants = participants;
        this.messages = messages;
    }

    public Chat(ArrayList<String> participants)
    {
        this(participants, new ArrayList<MessageResponse>());
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public List<MessageResponse> getMessages() {
        return messages;
    }
    public void addMessage(String message, String senderId) {
        int maxId = 0;
        for(MessageResponse m : messages) {
            if(m.getId() > maxId) {
                maxId = m.getId();
            }
        }
        messages.add(new MessageResponse(maxId + 1, message, senderId));
    }
}

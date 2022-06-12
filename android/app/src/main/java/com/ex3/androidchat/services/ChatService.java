package com.ex3.androidchat.services;

import com.ex3.androidchat.Client;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Message;
import com.ex3.androidchat.models.contacts.MessageResponse;

import java.util.ArrayList;
import java.util.List;

public class ChatService implements IChatService {

    private static ArrayList<Chat> chats = new ArrayList<Chat>();

    @Override
    public ArrayList<Chat> getAll() {
        return chats;
    }

    public static ArrayList<MessageResponse> toMessagesResponses(ArrayList<Message> messages) {
        ArrayList<MessageResponse> list = new ArrayList<>();
        for(Message m : messages) {
            list.add(new MessageResponse(m.getId(), m.getText(), m.getType(), m.getSenderUsername(),
                    m.getFileName(), m.getWrittenIn(), m.getSenderUsername().equals(Client.getUserId())));
        }
        return list;
    }

    public static void setChats(ArrayList<Chat> list) {
        chats = new ArrayList<>(list);
    }

    public static ArrayList<Chat> getChats() {
        return chats;
    }

    @Override
    public Chat getById(int id) {
        for (Chat chat : chats) {
            if (chat.getId() == id)
                return chat;
        }
        return null;
    }

    @Override
    public boolean create(Chat chat) {
        return chats.add(chat);
    }

    @Override
    public boolean update(Chat chat) {
        return delete(chat.getId()) && create(chat);
    }

    @Override
    public boolean delete(int chatId) {
        for (Chat chat : chats) {
            if (chat.getId() == chatId)
                chats.remove(chat);
            return true;
        }
        return false;
    }

    @Override
    public int getNewMsgIdInChat(int id) {
        int maxId = 0;
        for (Chat chat : chats) {
            if (chat.getId() > maxId)
                maxId = chat.getId();
        }
        return maxId + 1;
    }

    @Override
    public boolean addMessage(int chatId, Message message) {
        Chat chat = getById(chatId);
        if (chat == null) return false;
        return chat.getMessages().add(message);
    }

    @Override
    public Chat getChatByParticipants(String username, String other) {
        for (Chat chat : chats) {
            if (chat.getParticipants().contains(username) &&
                    chat.getParticipants().contains(other))
                return chat;
        }
        return null;
    }

    @Override
    public List<Message> getAllMessages(String username, String other) {
        Chat chat = getChatByParticipants(username, other);
        if (chat == null) return null;
        return chat.getMessages();
    }
}

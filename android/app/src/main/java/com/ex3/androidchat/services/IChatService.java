package com.ex3.androidchat.services;

import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Message;

import java.util.ArrayList;
import java.util.List;

public interface IChatService {
    ArrayList<Chat> getAll();
    Chat getById(int id);
    boolean create(Chat chat);
    boolean update(Chat chat);
    boolean delete(int chatId);
    int getNewMsgIdInChat(int id);
    boolean addMessage(int chatId, Message message);
    Chat getChatByParticipants(String username, String other);
    List<Message> getAllMessages(String username, String other);
}

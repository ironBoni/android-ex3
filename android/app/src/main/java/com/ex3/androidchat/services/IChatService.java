package com.ex3.androidchat.services;

import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Message;

import java.util.ArrayList;

public interface IChatService {
    ArrayList<Chat> GetAll();
    Chat GetById(int id);
    boolean Create(Chat chat);
    boolean Update(Chat chat);
    boolean Delete(int chatId);
    int GetNewMsgIdInChat(int id);
    boolean AddMessage(int chatId, Message message);
    Chat GetChatByParticipants(String username, String other);
    ArrayList<Message> GetAllMessages(String username, String other);
}

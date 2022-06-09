package com.ex3.androidchat.services;

import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.contacts.MessageResponse;

import java.util.ArrayList;
import java.util.List;

public interface IChatService {
    ArrayList<Chat> GetAll();
    Chat GetById(int id);
    boolean Create(Chat chat);
    boolean Update(Chat chat);
    boolean Delete(int chatId);
    int GetNewMsgIdInChat(int id);
    boolean AddMessage(int chatId, MessageResponse message);
    Chat GetChatByParticipants(String username, String other);
    List<MessageResponse> GetAllMessages(String username, String other);
}

package com.ex3.androidchat.services;

import com.ex3.androidchat.Client;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Message;
import com.ex3.androidchat.models.contacts.MessageResponse;

import java.util.ArrayList;
import java.util.List;

public class ChatService implements IChatService {
/*
    private static ArrayList<String> ronAndNoam = new ArrayList<String>(Arrays.asList("ron", "noam"));
    private static ArrayList<String> ronAndDvir = new ArrayList<String>(Arrays.asList("ron", "dvir"));
    private static ArrayList<String> ronAndDan = new ArrayList<String>(Arrays.asList("ron", "dan"));
    private static ArrayList<String> ronAndIdan = new ArrayList<String>(Arrays.asList("ron", "idan"));
    private static ArrayList<String> ronAndHadar = new ArrayList<String>(Arrays.asList("ron", "hadar"));
    private static ArrayList<String> noamAndDvir = new ArrayList<String>(Arrays.asList("noam", "dvir"));

    private static ArrayList<Message> ronNoamMessages = new ArrayList<Message>(Arrays.asList(
            new Message(1, "text", "my name is Noam.", "noam", "04.06.2021, 09:56:00", true),
            new Message(2, "text", "my name is Ron.", "ron", "04.06.2021 10:05:00", true),
            new Message(3, "text", "Nice to meet you!", "ron", "04.08.2021 10:30:00", true)));

    private static ArrayList<Message> ronDvirMessages = new ArrayList<Message>(Arrays.asList(
            new Message(1, "text", "my name is Dvir.", "dvir", "04.06.2021, 09:56:00", true),
            new Message(2, "text", "my name is Ron.", "ron", "04.06.2021 10:05:00", true),
            new Message(3, "text", "Nice to meet you!", "ron", "04.08.2021 10:30:00", true)));

    private static ArrayList<Message> ronDanMessages = new ArrayList<Message>(Arrays.asList(
            new Message(1, "text", "my name is Dan.", "dan", "04.06.2021, 09:56:00", true),
            new Message(2, "text", "my name is Ron.", "ron", "04.06.2021 10:05:00", true),
            new Message(3, "text", "Nice to meet you!", "ron", "04.08.2021 10:30:00", true)));

    private static ArrayList<Message> ronIdanMessages = new ArrayList<Message>(Arrays.asList(
            new Message(1, "text", "my name is Dan.", "dan", "04.06.2021, 09:56:00", true),
            new Message(2, "text", "my name is Ron.", "ron", "04.06.2021 10:05:00", true),
            new Message(3, "text", "Nice to meet you!", "ron", "04.08.2021 10:30:00", true)));

    private static ArrayList<Message> ronHadarMessages = new ArrayList<Message>(Arrays.asList(
            new Message(1, "text", "my name is Hadar.", "hadar", "04.06.2021, 09:56:00", true),
            new Message(2, "text", "my name is Ron.", "ron", "04.06.2021 10:05:00", true),
            new Message(3, "text", "Nice to meet you!", "ron", "04.08.2021 10:30:00", true)));

    private static ArrayList<Message> dvirNoamMessages = new ArrayList<Message>(Arrays.asList(
            new Message(1, "text", "my name is Noam.", "noam", "04.06.2021, 09:56:00", true),
            new Message(2, "text", "my name is Dvir.", "dvir", "04.06.2021 10:05:00", true),
            new Message(3, "text", "Nice to meet you!", "dvir", "04.08.2021 10:30:00", true)));
*/

    private static ArrayList<Chat> chats = new ArrayList<Chat>();
            /*(Arrays.asList(new Chat(1, ronAndNoam, ronNoamMessages),
                    new Chat(2, ronAndDvir, ronDvirMessages),
                    new Chat(3, ronAndDan, ronDanMessages),
                    new Chat(5, ronAndIdan, ronIdanMessages),
                    new Chat(6, ronAndHadar, ronHadarMessages),
                    new Chat(7, noamAndDvir, dvirNoamMessages)));
*/
    @Override
    public ArrayList<Chat> GetAll() {
        return chats;
    }

    public static ArrayList<MessageResponse> toMessagesResponses(ArrayList<Message> messages) {
        ArrayList<MessageResponse> list = new ArrayList<>();
        for(Message m : messages) {
            list.add(new MessageResponse(m.getId(), m.getText(), m.getType(), m.getSenderUsername(),
                    m.getFileName(), m.getWrittenIn(), m.getSenderUsername() == Client.getUserId()));
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
    public Chat GetById(int id) {
        for (Chat chat : chats) {
            if (chat.getId() == id)
                return chat;
        }
        return null;
    }

    @Override
    public boolean Create(Chat chat) {
        return chats.add(chat);
    }

    @Override
    public boolean Update(Chat chat) {
        return Delete(chat.getId()) && Create(chat);
    }

    @Override
    public boolean Delete(int chatId) {
        for (Chat chat : chats) {
            if (chat.getId() == chatId)
                chats.remove(chat);
            return true;
        }
        return false;
    }

    @Override
    public int GetNewMsgIdInChat(int id) {
        int maxId = 0;
        for (Chat chat : chats) {
            if (chat.getId() > maxId)
                maxId = chat.getId();
        }
        return maxId + 1;
    }

    @Override
    public boolean AddMessage(int chatId, Message message) {
        Chat chat = GetById(chatId);
        if (chat == null) return false;
        return chat.getMessages().add(message);
    }

    @Override
    public Chat GetChatByParticipants(String username, String other) {
        for (Chat chat : chats) {
            if (chat.getParticipants().contains(username) &&
                    chat.getParticipants().contains(other))
                return chat;
        }
        return null;
    }

    @Override
    public List<Message> GetAllMessages(String username, String other) {
        Chat chat = GetChatByParticipants(username, other);
        if (chat == null) return null;
        return chat.getMessages();
    }
}

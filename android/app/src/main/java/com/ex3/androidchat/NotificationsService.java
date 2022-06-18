package com.ex3.androidchat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ex3.androidchat.adapters.ContactsAdapter;
import com.ex3.androidchat.adapters.ConversationAdapter;
import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;
import com.ex3.androidchat.database.MessageDB;
import com.ex3.androidchat.database.MessageDao;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.Utils;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.ex3.androidchat.services.ChatService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationsService extends FirebaseMessagingService {
    public static ConversationAdapter conversationAdapter;
    public static ContactsAdapter contactsAdapter;

    private void notifyListeners(String fromUserId, String content) {
        if (contactsAdapter != null) {
            ArrayList<Contact> oldContacts = contactsAdapter.getContacts();
            if (oldContacts == null) return;
            for (Contact c : oldContacts) {
                if (c.getContactId().equals(fromUserId)) {
                    c.last = content;
                    java.util.Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    SimpleDateFormat formatterStr = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    c.lastdate = formatter.format(date);
                    c.lastdateStr = formatterStr.format(date);
                    break;
                }
            }
            if (oldContacts != null) {
                Client.mainActivity.runOnUiThread(() -> contactsAdapter.setContacts(oldContacts));
            }
        }

        if (conversationAdapter != null) {
            ArrayList<MessageResponse> messages = conversationAdapter.getMessages();
            if (messages == null) return;
            int maxId = 0;
            for (MessageResponse m : messages) {
                if (m.getId() > maxId) {
                    maxId = m.getId();
                }
            }

            ChatService service = new ChatService();
            int chatId = 1;
            try {
                try {
                    MessageDao messageDao = MessageDB.insert(this).messageDao();
                    chatId = messageDao.isUserExits(fromUserId).get(0).chatId;
                } catch (Exception ex) {
                    Chat chat = service.getChatByParticipants(fromUserId, Client.getUserId());
                    if (chat != null) {
                        chatId = chat.getId();
                    } else {
                        chatId = service.getAll().get(0).id;
                    }
                }
            } catch(Exception ex) {
                Log.e("error", ex.toString());
            }

            messages.add(new MessageResponse(fromUserId, content, Client.getUserId(), chatId));

            // don't update if the notification is about my message
            if (fromUserId.equals(Client.getUserId()))
                return;
            if (Client.conversationActivity != null && Client.getFriendId().equals(fromUserId)) {
                Client.conversationActivity.runOnUiThread(() -> conversationAdapter.setMessages(messages));
            } else if (Client.mainActivity != null && Client.getFriendId().equals(fromUserId)) {
                Client.mainActivity.runOnUiThread(() -> conversationAdapter.setMessages(messages));
            }

            updateDao(content, fromUserId);
        }
    }

    private void updateDao(String msg, String friendId) {
        try {
            ContactDao contactDao = AppDB.getContactDBInstance(this).contactDao();
            contactDao.updateLast(msg, friendId);
            java.util.Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            contactDao.updateDate(formatter.format(date), friendId);
            ChatService service = new ChatService();

            MessageDao messageDao = MessageDB.insert(this).messageDao();
            int chatId;
            try {
                chatId = messageDao.isUserExits(friendId).get(0).chatId;
            } catch (Exception ex) {
                Chat chat = service.getChatByParticipants(friendId, Client.getUserId());
                if (chat != null) {
                    chatId = chat.getId();
                } else {
                    chatId = service.getAll().get(0).id;
                }
            }
            messageDao.insert(new MessageResponse(friendId, msg, Client.getUserId(), chatId));

        } catch (Exception ex) {
            Log.d("Notification", ex.toString());
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() == null) return;

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        String from = Utils.getLastWord(remoteMessage.getNotification().getTitle());
        String content = remoteMessage.getNotification().getBody();
        notifyListeners(from, content);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "name", importance);
            channel.setDescription("Demo channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
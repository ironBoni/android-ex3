package com.ex3.androidchat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ex3.androidchat.adapters.ContactsAdapter;
import com.ex3.androidchat.adapters.ConversationAdapter;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.Utils;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationsService extends FirebaseMessagingService {
    public static ConversationAdapter conversationAdapter;
    public static ContactsAdapter contactsAdapter;

    private void notifyListeners(String fromUserId, String content) {
        if(contactsAdapter != null) {
            ArrayList<Contact> oldContacts = contactsAdapter.getContacts();
            if(oldContacts == null) return;
            for(Contact c : oldContacts) {
                if(c.getContactId().equals(fromUserId)) {
                    c.last = content;
                    java.util.Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    c.lastdate = formatter.format(date);
                    break;
                }
            }
            if(oldContacts != null) {
                Client.mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contactsAdapter.setContacts(oldContacts);
                    }
                });
            }
        }

        if(conversationAdapter != null) {
            ArrayList<MessageResponse> messages = conversationAdapter.getMessages();
            if(messages == null) return;
            int maxId = 0;
            for(MessageResponse m : messages) {
                if(m.getId() > maxId) {
                    maxId = m.getId();
                }
            }
            messages.add(new MessageResponse(maxId + 1, content, fromUserId));
            if(messages != null && Client.getFriendId().equals(fromUserId)) {
                Client.conversationActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        conversationAdapter.setMessages(messages);
                    }
                });
            }
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getNotification() == null) return;

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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "name", importance);
            channel.setDescription("Demo channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
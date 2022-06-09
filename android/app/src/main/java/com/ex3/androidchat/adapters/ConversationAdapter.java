package com.ex3.androidchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ex3.androidchat.Client;
import com.ex3.androidchat.R;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.ex3.androidchat.services.ChatService;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter {
    List<MessageResponse> messages;
    Context context;
    int viewTypeSender = 1;
    int viewTypeReceiver = 2;
    ChatService service;
    public ConversationAdapter(List<MessageResponse> messages, Context context) {
        this.messages = messages;
        this.context = context;
        this.service = new ChatService();
    }

    public void addNewMessage(String text) {
        Chat chat = service.GetChatByParticipants(Client.getUserId(), Client.getFriendId());
        int maxId = 0;
        for(MessageResponse m : chat.getMessages()) {
            if(m.getId() > maxId) {
                maxId = m.getId();
            }
        }
        int newId = maxId + 1;
        messages.add(new MessageResponse(newId, text, Client.getUserId()));
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == viewTypeSender) {
            View senderLayout = LayoutInflater.from(context).inflate(R.layout.msg_sender,parent, false);
            return new ViewHolderSend(senderLayout);
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.msg_reciever, parent, false);
        return new ViewHolderReceive(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageResponse msg = messages.get(position);

        if(holder.getClass() == ViewHolderSend.class) {
            ((ViewHolderSend)holder).messageSent.setText(msg.getContent());
        } else {
            ((ViewHolderReceive)holder).messageReceived.setText(msg.getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getSenderUsername().equals(Client.getUserId())) {
            return viewTypeSender;
        }
        return viewTypeReceiver;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolderReceive extends RecyclerView.ViewHolder {

        TextView messageReceived, timeReceived;
        public ViewHolderReceive(@NonNull View itemView) {
            super(itemView);
            messageReceived = itemView.findViewById(R.id.txtRecieve);
            timeReceived = itemView.findViewById(R.id.txtTimeReceived);
        }
    }

    public class ViewHolderSend extends RecyclerView.ViewHolder {

        TextView messageSent, sentTime;
        public ViewHolderSend(@NonNull View itemView) {
            super(itemView);
            messageSent = itemView.findViewById(R.id.textSent);
            sentTime = itemView.findViewById(R.id.timeSent);
        }
    }
}

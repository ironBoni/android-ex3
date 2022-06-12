package com.ex3.androidchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.ex3.androidchat.Client;
import com.ex3.androidchat.R;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Message;
import com.ex3.androidchat.models.Utils;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.ex3.androidchat.services.ChatService;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter {
    ArrayList<MessageResponse> messages;
    MutableLiveData<ArrayList<MessageResponse>> liveMessages;
    RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public MutableLiveData<ArrayList<MessageResponse>> getLiveMessages() {
        return liveMessages;
    }

    public void setLiveMessages(MutableLiveData<ArrayList<MessageResponse>> liveMessages) {
        this.liveMessages = liveMessages;
        notifyDataSetChanged();
    }

    public ArrayList<MessageResponse> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageResponse> messages) {
        this.messages = messages;
        this.liveMessages.postValue(messages);
        notifyDataSetChanged();

        if(recyclerView != null)
            recyclerView.scrollToPosition(getItemCount() - 1);
    }

    Context context;
    int viewTypeSender = 1;
    int viewTypeReceiver = 2;
    ChatService service;

    public ConversationAdapter(ArrayList<MessageResponse> messages, Context context, RecyclerView recyclerView) {
        this.messages = messages;

        liveMessages = new MutableLiveData<>();
        liveMessages.setValue(messages);
        this.context = context;
        this.service = new ChatService();
        this.recyclerView = recyclerView;
    }

    public void addNewMessage(String text) {
        Chat chat = service.getChatByParticipants(Client.getUserId(), Client.getFriendId());
        int maxId = 0;
        for (Message m : chat.getMessages()) {
            if (m.getId() > maxId) {
                maxId = m.getId();
            }
        }
        int newId = maxId + 1;
        messages.add(new MessageResponse(newId, text, Client.getUserId()));
        setMessages(messages);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == viewTypeSender) {
            View senderLayout = LayoutInflater.from(context).inflate(R.layout.msg_sender, parent, false);
            return new ViewHolderSend(senderLayout);
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.msg_reciever, parent, false);
        return new ViewHolderReceive(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageResponse msg = messages.get(position);

        if (holder.getClass() == ViewHolderSend.class) {
            ViewHolderSend sendHolder = ((ViewHolderSend) holder);
            sendHolder.messageSent.setText(msg.getContent());
            sendHolder.sentTime.setText(Utils.getHour(msg.getCreatedDateStr()));
        } else {
            ViewHolderReceive receiveHolder = ((ViewHolderReceive) holder);
            receiveHolder.messageReceived.setText(msg.getContent());
            receiveHolder.timeReceived.setText(Utils.getHour(msg.getCreatedDateStr()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderUsername().equals(Client.getUserId())) {
            return viewTypeSender;
        }
        return viewTypeReceiver;
    }

    @Override
    public int getItemCount() {
        if (messages == null) return 0;
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

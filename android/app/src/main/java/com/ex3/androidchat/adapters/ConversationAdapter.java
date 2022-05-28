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
import com.ex3.androidchat.models.Message;

import java.util.ArrayList;

public class ConversationAdapter extends RecyclerView.Adapter {
    ArrayList<Message> messages;
    Context context;
    int viewTypeSender = 1;
    int viewTypeReceiver = 2;
    public ConversationAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
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
        Message msg = messages.get(position);

        if(holder.getClass() == ViewHolderSend.class) {
            ((ViewHolderSend)holder).messageSent.setText(msg.getText());
        } else {
            ((ViewHolderReceive)holder).messageReceived.setText(msg.getText());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getSenderUsername().equals(Client.getUser())) {
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

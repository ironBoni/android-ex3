package com.ex3.androidchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ex3.androidchat.ConversationActivity;
import com.ex3.androidchat.R;
import com.ex3.androidchat.models.Contact;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    ArrayList<Contact> contacts;
    Context context;

    public ContactsAdapter(ArrayList<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.nickName.setText(contact.getName());
        holder.lastMsgTime.setText(contact.getLastdate());
        holder.picture.setImageResource(R.drawable.default_avatar);
        holder.lastMsg.setText(contact.getLast());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConversationActivity.class);
                intent.putExtra("id", contact.getId());
                intent.putExtra("nickname", contact.getName());
                intent.putExtra("server", contact.getServer());
                intent.putExtra("image", contact.getProfileImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView nickName, lastMsg, lastMsgTime, lastMsgHour;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.image);
            nickName = itemView.findViewById(R.id.txtViewNickname);
            lastMsg = itemView.findViewById(R.id.lastMsg);
            lastMsgTime = itemView.findViewById(R.id.lastMsgTime);
            lastMsgHour = itemView.findViewById(R.id.lastMsgHour);
        }
    }
}

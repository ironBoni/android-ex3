package com.ex3.androidchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ex3.androidchat.AndroidChat;
import com.ex3.androidchat.ConversationActivity;
import com.ex3.androidchat.R;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.services.GetByAsyncTask;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    ArrayList<Contact> contacts;
    Context context;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public ContactsAdapter(ArrayList<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(AndroidChat.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

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

        String date = contact.getLastdate().split(" ")[0].substring(0,5);
        String hour = contact.getLastdate().split(" ")[1];

        //holder.lastMsgTime.setText(hour + "\n" + date);
        holder.lastMsgTime.setText(date);
        holder.lastMsgHour.setText(hour);
        holder.lastMsg.setText(contact.getLast());
        holder.nickName.setText(contact.getName());

         new GetByAsyncTask((ImageView) holder.picture).execute(contact.getProfileImage());
        //holder.picture.setImageResource(R.drawable.default_avatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ConversationActivity.class);
                intent.putExtra("id", contact.getContactId());
                intent.putExtra("nickname", contact.getName());
                intent.putExtra("server", contact.getServer());
                intent.putExtra("image", contact.getProfileImage());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

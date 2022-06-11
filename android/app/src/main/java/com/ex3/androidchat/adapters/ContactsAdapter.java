package com.ex3.androidchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.ex3.androidchat.AndroidChat;
import com.ex3.androidchat.Client;
import com.ex3.androidchat.ConversationActivity;
import com.ex3.androidchat.MainActivity;
import com.ex3.androidchat.R;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.events.IEventListener;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.services.GetByAsyncTask;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements IEventListener<String> {
    ArrayList<Contact> contacts;
    MutableLiveData<ArrayList<Contact>> liveContacts;

    public MutableLiveData<ArrayList<Contact>> getLiveContacts() {
        return liveContacts;
    }

    public void setLiveContacts(MutableLiveData<ArrayList<Contact>> liveContacts) {
        this.liveContacts = liveContacts;
        notifyDataSetChanged();
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> newContacts) {
        this.contacts = newContacts;
        this.liveContacts.postValue(newContacts);
        notifyDataSetChanged();
    }

    Context context;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    private MainActivity listener;
    private final int MAX_LENGTH_OF_MESSAGE = 18;
    private final int MAX_LENGTH_OF_PREVIEW = 14;

    public void addListener(MainActivity activity) {
        listener = activity;
    }

    private void notifyListeners(Contact contact) {
        listener.update(contact);
    }
    public ContactsAdapter(ArrayList<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.liveContacts = new MutableLiveData<>();
        liveContacts.postValue(contacts);
        this.context = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
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

        String date;
        String hour;

        if (contact.getLastdate() != null && contact.getLastdate().split(" ").length == 2
                && contact.getLastdate().split(" ")[0].length() >= 6) {
            date = contact.getLastdate().split(" ")[0].substring(0, 5);
            hour = contact.getLastdate().split(" ")[1];
        } else {
            date = "";
            hour = "";
        }

        //holder.lastMsgTime.setText(hour + "\n" + date);
        holder.lastMsgTime.setText(date);
        holder.lastMsgHour.setText(hour);

        if(contact.getLast() == null)
            holder.lastMsg.setText(null);
        else if(contact.getLast().length() < MAX_LENGTH_OF_MESSAGE)
            holder.lastMsg.setText(contact.getLast());
        else {
            String msg = contact.getLast().substring(0, MAX_LENGTH_OF_PREVIEW);
            msg = msg + "...";
            holder.lastMsg.setText(msg);
        }
        holder.nickName.setText(contact.getName());

        new GetByAsyncTask((ImageView) holder.picture).execute(contact.getProfileImage());
        //holder.picture.setImageResource(R.drawable.default_avatar);

        if (AndroidChat.context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Client.setFriendId(contact.getContactId());
                    Client.setFriendNickname(contact.getName());
                    Client.setFriendServer(contact.getServer());
                    Client.setFriendImage(contact.getProfileImage());
                    notifyListeners(contact);
                }
            });
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client.setFriendId(contact.getContactId());
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

    @Override
    public void update(String element) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
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

            if (AndroidChat.context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                /*picture.setMaxWidth(40);
                picture.setMaxWidth(40);*/
            }
        }
    }
}

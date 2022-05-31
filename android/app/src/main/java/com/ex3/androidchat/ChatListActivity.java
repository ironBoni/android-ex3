package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ex3.androidchat.adapters.ContactsAdapter;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.User;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {
    ArrayList<Contact> contacts = new ArrayList<>();
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_list);

        RecyclerView recyclerView = findViewById(R.id.rvChatList);

        contacts = currentUser.getContacts();
        ContactsAdapter adapter = new ContactsAdapter(contacts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCurrentUser() {

    }
}
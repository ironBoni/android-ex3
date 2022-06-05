package com.ex3.androidchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ex3.androidchat.adapters.ContactsAdapter;
import com.ex3.androidchat.adapters.FragAdapter;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.User;
import com.ex3.androidchat.services.UserService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ViewPager pager;
    TabLayout tabMenu;

    ArrayList<Contact> contacts = new ArrayList<>();
    User currentUser;
    FloatingActionButton addContact;
     UserService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.happy_chat);

        addContact = findViewById(R.id.addContact);
        RecyclerView recyclerView = findViewById(R.id.rvChatList);
        service = new UserService();
        service.loadContacts();
//        contacts = currentUser.getContacts();
        //static array
        contacts = service.getById(Client.getUser()).getContacts();
        /*contacts.add(new Contact("0","hadar","localhost:3000", "hey there",0,"31.05.22"));
        contacts.add(new Contact("1","noam","localhost:3000", "hey there",1,"31.05.22"));
        contacts.add(new Contact("2","dvir","localhost:3000", "hey there",2,"31.05.22"));
        contacts.add(new Contact("3","linda","localhost:3000", "hey there",3,"31.05.22"));*/
        ContactsAdapter adapter = new ContactsAdapter(contacts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

            addContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                    startActivity(intent);
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf =getMenuInflater();
        inf.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.settings) {
            // settings
        } else {
            // logout
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
package com.ex3.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.ex3.androidchat.adapters.ContactsAdapter;
import com.ex3.androidchat.api.ContactsAPI;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.User;
import com.ex3.androidchat.services.ChatService;
import com.ex3.androidchat.services.UserService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ViewPager pager;
    TabLayout tabMenu;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    ArrayList<Contact> contacts = new ArrayList<>();
    User currentUser;
    FloatingActionButton addContact;
     UserService service;
     ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidChat.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.happy_chat);

        AppDB db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ContactDB")
                .allowMainThreadQueries()
                .build();

        contactDao = db.contactDao();
        retrofit = new Retrofit.Builder()
                .baseUrl(AndroidChat.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        addContact = findViewById(R.id.addContact);
        RecyclerView recyclerView = findViewById(R.id.rvChatList);
        service = new UserService();
        //bservice.loadContacts();

        Call<List<Chat>> call = webServiceAPI.getChats(Client.getToken());
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                ChatService.setChats(response.body());
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });

        Call<List<Contact>> callContacts = webServiceAPI.getContacts(Client.getToken());
        callContacts.enqueue(new Callback<List<Contact>>() {
         @Override
         public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
             contacts = new ArrayList<>(response.body());
             ContactsAPI contactsAPI = new ContactsAPI();
             contactsAPI.get();

             ContactsAdapter adapter = new ContactsAdapter(contacts, getApplicationContext());
             recyclerView.setAdapter(adapter);
             recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
             addContact.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                     startActivity(intent);
                 }
             });

         }

         @Override
         public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
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
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else {
            // logout
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
package com.ex3.androidchat;

import static java.util.UUID.randomUUID;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.ex3.androidchat.api.ContactsAPI;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;
import com.ex3.androidchat.events.IEventListener;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.Utils;
import com.ex3.androidchat.models.contacts.ContactRequest;
import com.ex3.androidchat.models.invitations.InvitationRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddUserActivity extends AppCompatActivity implements IEventListener<String> {
    Button cAdd;
    ImageView backButton;
    EditText cName, cNickname, cServer;
    ContactDao contactDao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backButton = findViewById(R.id.btnBackAddUser);
        setContentView(R.layout.activity_add_user);
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        contactDao = AppDB.getContactDBInstance(this).contactDao();

        cAdd = findViewById(R.id.cAdd);
        cName = findViewById(R.id.cName);
        cNickname = findViewById(R.id.cNickname);
        cServer = findViewById(R.id.cServer);

        int currentNightMode = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            cName.setTextColor(getResources().getColor(R.color.black));
            cNickname.setTextColor(getResources().getColor(R.color.black));
            cServer.setTextColor(getResources().getColor(R.color.black));
        }

        backButton = findViewById(R.id.btnBackAddUser);
        backButton.setOnClickListener(v -> {
                Intent intent = new Intent(AddUserActivity.this, MainActivity.class);
                startActivity(intent);
        });
        cAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send name to server and get contact, then add it to list
                String hisId = cName.getText().toString();
                String hisNickname = cNickname.getText().toString();
                String hisServer = cServer.getText().toString();
                String id = randomUUID().toString();

                //contactDao.insert(new Contact(id, hisId, hisNickname, hisServer));
                sendInvitationToHisServer(hisId, hisNickname, hisServer);
                addContactInServer(hisId, hisNickname, hisServer);

                Intent intent = new Intent(AddUserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addContactInServer(String hisId, String hisNickname, String hisServer) {

        Call<Void> call = webServiceAPI.createContact(new ContactRequest(hisId, hisNickname, hisServer), Client.getToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //adding contact deletes room, and it will update again
                List<Contact> contacts = contactDao.index();
                contactDao.deleteList(contacts);
                Log.d("retrofit", "success in adding the contact.");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });
    }

    private void sendInvitationToHisServer(String friendId, String friendNickname, String friendServer) {
        friendServer = Utils.getFullServerUrl(friendServer);
        friendServer = Utils.getAndroidServer(friendServer);
        Retrofit hisRetrofit = new Retrofit.Builder()
                .baseUrl(friendServer + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WebServiceAPI hisServiceAPI = hisRetrofit.create(WebServiceAPI.class);

        Call<Void> call = hisServiceAPI.sendInvitation(new InvitationRequest(Client.getUserId(), friendId, Client.getMyServer()));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("retrofit", "Sent invitation successfully.");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });
    }

    @Override
    public void update(String element) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
}
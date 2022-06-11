package com.ex3.androidchat;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.ex3.androidchat.adapters.ContactsAdapter;
import com.ex3.androidchat.adapters.ConversationAdapter;
import com.ex3.androidchat.api.ContactsAPI;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;
import com.ex3.androidchat.events.IEventListener;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.User;
import com.ex3.androidchat.models.Utils;
import com.ex3.androidchat.models.contacts.GetUserDetailsResponse;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.ex3.androidchat.models.contacts.SendMessageRequest;
import com.ex3.androidchat.models.contacts.UserModel;
import com.ex3.androidchat.models.transfer.TransferRequest;
import com.ex3.androidchat.services.ChatService;
import com.ex3.androidchat.services.GetByAsyncTask;
import com.ex3.androidchat.services.UserService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements IEventListener<String> {
    ViewPager pager;
    TabLayout tabMenu;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    ArrayList<Contact> contacts = new ArrayList<>();
    User currentUser;
    FloatingActionButton addContact;
    UserService service;
    ContactDao contactDao;
    ImageView backButton, btnSendConv;
    ChatService chatService = new ChatService();
    Chat conversition;
    ArrayList<MessageResponse> messages;

    // for landscape.

    private void addNewMessage(String friendId, String text) {
        Chat conversation = chatService.GetChatByParticipants(Client.getUserId(), friendId);
        conversation.addMessage(text, Client.getUserId());
    }

    private void sendMessageToForeignServer(String friendId, String msg, String hisServer) {
        hisServer = Utils.getAndroidServer(hisServer);
        Retrofit hisRetrofit = new Retrofit.Builder()
                .baseUrl(hisServer + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WebServiceAPI hisServiceAPI = hisRetrofit.create(WebServiceAPI.class);

        Call<Void> call = hisServiceAPI.transfer(new TransferRequest(Client.getUserId(), Client.getFriendId(), msg));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("retrofit", "Transferred successfully");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("retrofit", t.getMessage().toString());
            }
        });
    }

    private void sendMessageToServer(String friendId, String msg) {
        Call<Void> call = webServiceAPI.sendMessage(Client.getFriendId(), new SendMessageRequest(msg), Client.getToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("retrofit", "sent message to server successfully.");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });

        Call<GetUserDetailsResponse> getServerCall = webServiceAPI.getServerByUsername(friendId, Client.getToken());
        getServerCall.enqueue(new Callback<GetUserDetailsResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailsResponse> call, Response<GetUserDetailsResponse> response) {
                if(response.body() == null) {
                    Log.d("retrofit", "got empty body");
                    return;
                }
                String hisServer = response.body().server;
                UserService userService = new UserService();
                hisServer = userService.getFullServerUrl(hisServer);
                String myServer = Client.getMyServer();
                if(myServer.indexOf(hisServer) < 0 && hisServer.indexOf(myServer) < 0) {
                    sendMessageToForeignServer(Client.getFriendId(), msg, hisServer);
                }
            }

            @Override
            public void onFailure(Call<GetUserDetailsResponse> call, Throwable t) {
                Log.e(getString(R.string.retrofit), t.getMessage());
            }
        });
    }


    private void sendMessage(String friendId, ConversationAdapter adapter) {
        EditText txtMsg = (EditText) findViewById(R.id.txtEnterMsgConv);
        String msg = txtMsg.getText().toString();
        adapter.addNewMessage(msg);
        txtMsg.setText("");
        adapter.notifyDataSetChanged();
        sendMessageToServer(Client.getFriendId(), msg);

        RecyclerView recyclerViewConv = findViewById(R.id.messagesViewConv);
        if(recyclerViewConv != null)
            recyclerViewConv.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void continueOnCreateOnResponse(ArrayList<MessageResponse> allMessages, String friendId) {
        RecyclerView recyclerViewConv = findViewById(R.id.messagesViewConv);

        messages = allMessages;
        if(messages == null) {
            Chat chat = chatService.GetChatByParticipants(Client.getUserId(), Client.getFriendId());

            if(chat.getMessages() == null) return;
            messages = ChatService.toMessagesResponses(new ArrayList<>(chat.getMessages()));
        }

        if(messages == null) return;
        ConversationAdapter adapter = new ConversationAdapter(messages,MainActivity.this);
        recyclerViewConv.scrollToPosition(adapter.getItemCount() - 1);
        recyclerViewConv.setAdapter(adapter);
        recyclerViewConv.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        backButton = findViewById(R.id.btnBackConv);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSendConv = findViewById(R.id.btnSendConvConv);
        btnSendConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(Client.getFriendId(), adapter);
            }
        });

        EditText txtMsg = (EditText) findViewById(R.id.txtEnterMsgConv);
        txtMsg.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage(Client.getFriendId(), adapter);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidChat.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.happy_chat);
        //firebase
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
        });

        service = new UserService();
        TextView txtNickname = findViewById(R.id.txtViewNickname);
        txtNickname.setText(Client.getUserId());

        AppDB db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ContactDB")
                .allowMainThreadQueries()
                .build();

        contactDao = db.contactDao();
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        Call<UserModel> callUser = webServiceAPI.getUser(Client.getUserId(), Client.getToken());
        callUser.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                updateImage(response.body());
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e(getApplicationContext().getString(R.string.retrofit), t.getMessage());
            }
        });

        if (AndroidChat.context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            String friendId =  Client.getFriendId();
            String nickname =  Client.getFriendNickname();
            String server =  Client.getFriendServer();
            String image =  Client.getFriendServer();

            //beginConversationLandScape(friendId, nickname, image);
        }

        addContact = findViewById(R.id.addContact);
        RecyclerView recyclerView = findViewById(R.id.rvChatList);
        service = new UserService();
        //bservice.loadContacts();

        Call<ArrayList<Chat>> call = webServiceAPI.getChats(Client.getToken());
        call.enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(Call<ArrayList<Chat>> call, Response<ArrayList<Chat>> response) {
                try {
                    ChatService.setChats(response.body());
                } catch(Exception exception) {
                    Log.e("main activity", exception.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Chat>> call, Throwable t) {
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
                adapter.addListener(MainActivity.this);
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

    private void beginConversationLandScape(String friendId, String nickname, String image) {
        TextView contactNickname = findViewById(R.id.contactNicknameConv);
        contactNickname.setText(nickname);
        ImageView view = findViewById(R.id.user_imageConv);
        new GetByAsyncTask((ImageView) view).execute(image);

        RecyclerView recyclerView = findViewById(R.id.messagesViewConv);
        try {
            conversition = chatService.GetChatByParticipants(Client.getUserId(), friendId);
        } catch(Exception ex) {
            Log.e("Conversation", ex.getMessage());
            Toast.makeText(this, "Contact could not be loaded.", Toast.LENGTH_SHORT).show();
        }
        //messages = conversition.getMessages();

        Call<ArrayList<MessageResponse>> allMessages = webServiceAPI.getMessagesById(Client.getFriendId(), Client.getToken());
        allMessages.enqueue(new Callback<ArrayList<MessageResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MessageResponse>> call, Response<ArrayList<MessageResponse>> response) {
                continueOnCreateOnResponse(response.body(), Client.getFriendId());
            }

            @Override
            public void onFailure(Call<ArrayList<MessageResponse>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });
    }

    private void updateImage(UserModel user) {
        ImageView view = findViewById(R.id.imageUser);
        new GetByAsyncTask((ImageView) view).execute(user.getProfileImage());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else {
            // logout
            finish();
            Client.setToken("");
            Client.setUserId("");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void update(Contact c) {
        ImageView imageWelcome = findViewById(R.id.imgViewConv);
        if(imageWelcome == null)
            return;
        imageWelcome.setVisibility(View.GONE);

        RelativeLayout layout  = findViewById(R.id.convRelativeLayout);
        if(layout == null) return;
        layout.setVisibility(View.VISIBLE);
        beginConversationLandScape(c.getContactId(), c.getName(), c.getProfileImage());
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
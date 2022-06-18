package com.ex3.androidchat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

import com.ex3.androidchat.adapters.ContactsAdapter;
import com.ex3.androidchat.adapters.ConversationAdapter;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;
import com.ex3.androidchat.database.MessageDB;
import com.ex3.androidchat.database.MessageDao;
import com.ex3.androidchat.events.IEventListener;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.models.Utils;
import com.ex3.androidchat.models.contacts.GetUserDetailsResponse;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.ex3.androidchat.models.contacts.UserModel;
import com.ex3.androidchat.models.transfer.TransferRequest;
import com.ex3.androidchat.services.AsyncTaskDao;
import com.ex3.androidchat.services.ChatService;
import com.ex3.androidchat.services.GetByAsyncTask;
import com.ex3.androidchat.services.UserService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements IEventListener<String> {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    ArrayList<Contact> contacts = new ArrayList<>();
    FloatingActionButton addContact;
    UserService service;
    ContactDao contactDao;
    ImageView backButton, btnSendConv;
    ChatService chatService = new ChatService();
    Chat conversation;
    ArrayList<MessageResponse> messages;
    MessageDao messageDao;

    private void sendMessageToForeignServer(String friendId, String msg, String hisServer) {
        hisServer = Utils.getAndroidServer(hisServer);
        Retrofit hisRetrofit = new Retrofit.Builder()
                .baseUrl(hisServer + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WebServiceAPI hisServiceAPI = hisRetrofit.create(WebServiceAPI.class);

        if (Utils.getAndroidServer(hisServer).equals(Utils.getAndroidServer(Client.getMyServer())))
            return;
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
        Call<Void> call = webServiceAPI.transfer(new TransferRequest(Client.getUserId(), friendId, msg));
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
                if (response.body() == null) {
                    Log.d("retrofit", "got empty body");
                    return;
                }
                String hisServer = response.body().server;
                UserService userService = new UserService();
                hisServer = userService.getFullServerUrl(hisServer);
                String myServer = Client.getMyServer();
                if (myServer.indexOf(hisServer) < 0 && hisServer.indexOf(myServer) < 0) {
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
        contactDao = AppDB.getContactDBInstance(this).contactDao();
        EditText txtMsg = (EditText) findViewById(R.id.txtEnterMsgConv);
        String msg = txtMsg.getText().toString();
        contactDao.updateLast(msg, friendId);
        adapter.addNewMessage(msg);
        updateContactLastMsg(friendId, msg);
        txtMsg.setText("");
        adapter.notifyDataSetChanged();

        int chatId;
        try {
            chatId = messageDao.isUserExits(friendId).get(0).chatId;
        } catch (Exception ex) {
            Chat chat = chatService.getChatByParticipants(friendId, Client.getUserId());
            if (chat != null) {
                chatId = chat.getId();
            }
            else {
                chatId = chatService.getAll().get(0).id;
            }
        }
        messageDao.insert(new MessageResponse(Client.getUserId(), msg, friendId, chatId));
        sendMessageToServer(Client.getFriendId(), msg);

        RecyclerView recyclerViewConv = findViewById(R.id.messagesViewConv);
        if (recyclerViewConv != null)
            recyclerViewConv.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void updateContactLastMsg(String fromUserId, String content) {
        if(NotificationsService.contactsAdapter == null) return;

        ArrayList<Contact> oldContacts = NotificationsService.contactsAdapter.getContacts();
        if(oldContacts == null) return;
        for(Contact c : oldContacts) {
            if(c.getContactId().equals(fromUserId)) {
                c.last = content;
                java.util.Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                SimpleDateFormat formatterStr = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                c.lastdate = formatter.format(date);
                c.lastdateStr = formatterStr.format(date);
                break;
            }
        }
        NotificationsService.contactsAdapter.setContacts(oldContacts);
    }

    private void continueOnCreateOnResponse(ArrayList<MessageResponse> allMessages, String friendId) {
        RecyclerView recyclerViewConv = findViewById(R.id.messagesViewConv);

        messages = allMessages;
        if (messages == null) {
            Chat chat = chatService.getChatByParticipants(Client.getUserId(), Client.getFriendId());

            if (chat.getMessages() == null) return;
            messages = ChatService.toMessagesResponses(new ArrayList<>(chat.getMessages()));
        }

        if (messages == null) return;
        ConversationAdapter adapter = new ConversationAdapter(messages, MainActivity.this, recyclerViewConv);
        NotificationsService.conversationAdapter = adapter;
        recyclerViewConv.scrollToPosition(adapter.getItemCount() - 1);
        recyclerViewConv.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewConv.setLayoutManager(manager);

        backButton = findViewById(R.id.btnBackConv);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
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
        if (recyclerViewConv != null) {
            recyclerViewConv.scrollToPosition(adapter.getItemCount() - 1);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidChat.context = getApplicationContext();

        setContentView(R.layout.activity_main);
        Client.mainActivity = MainActivity.this;
        getSupportActionBar().setTitle(R.string.happy_chat);

        messageDao = MessageDB.insert(this).messageDao();

        service = new UserService();
        TextView txtNickname = findViewById(R.id.txtViewNickname);
        txtNickname.setText(Client.getUserId());

        contactDao = AppDB.getContactDBInstance(this).contactDao();

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
            String friendId = Client.getFriendId();
            String nickname = Client.getFriendNickname();
            String server = Client.getFriendServer();
            String image = Client.getFriendServer();
        }

        addContact = findViewById(R.id.addContact);
        RecyclerView recyclerView = findViewById(R.id.rvChatList);
        service = new UserService();

        Call<ArrayList<Chat>> call = webServiceAPI.getChats(Client.getToken());
        call.enqueue(new Callback<ArrayList<Chat>>() {
            @Override
            public void onResponse(Call<ArrayList<Chat>> call, Response<ArrayList<Chat>> response) {
                try {
                    ChatService.setChats(response.body());
                } catch (Exception exception) {
                    Log.e("main activity", exception.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Chat>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });
        //in the start, or when adding user
        if (userContact(contacts)) {
            contactDao.deleteAll();

            Call<List<Contact>> callContacts = webServiceAPI.getContacts(Client.getToken());
            callContacts.enqueue(new Callback<List<Contact>>() {
                @Override
                public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                    contactDao.insertList(new ArrayList<>(response.body()));

                    contacts = new ArrayList<>(response.body());
                    startContactList(recyclerView);

                    ContactsAdapter adapter = new ContactsAdapter(contacts, getApplicationContext());

                    NotificationsService.contactsAdapter = adapter;
                    adapter.addListener(MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    addContact.setOnClickListener(v -> {
                        Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }

                @Override
                public void onFailure(Call<List<Contact>> call, Throwable t) {
                    Log.e("retrofit", t.getMessage());
                }
            });
            try {
                updateImagesInDB(contactDao);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            contacts = (ArrayList<Contact>) contactDao.index();
            startContactList(recyclerView);
        }
    }

    private boolean userContact(ArrayList<Contact> contactsFinal) {
        ArrayList<Contact> listOfContacts = (ArrayList<Contact>) contactDao.index();
        if (listOfContacts.size() == 0) return true;
        for (Contact c : listOfContacts) {
            if (c.ofUser == null) return true;
            if (!c.ofUser.equals(Client.getUserId())) {
                return true;
            } else {
                contactsFinal.add(c);
            }
        }
        return false;
    }

    private void startContactList(RecyclerView recyclerView) {
        ContactsAdapter adapter = new ContactsAdapter(contacts, getApplicationContext());

        NotificationsService.contactsAdapter = adapter;
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

    private void beginConversationLandScape(String friendId, String nickname, String image) {
        messageDao = MessageDB.insert(this).messageDao();

        TextView contactNickname = findViewById(R.id.contactNicknameConv);
        contactNickname.setText(nickname);
        ImageView view = findViewById(R.id.user_imageConv);
        new GetByAsyncTask((ImageView) view).execute(image);

        RecyclerView recyclerView = findViewById(R.id.messagesViewConv);
        try {
            conversation = chatService.getChatByParticipants(Client.getUserId(), friendId);
        } catch (Exception ex) {
            Log.e("Conversation", ex.getMessage());
            Toast.makeText(this, "Contact could not be loaded.", Toast.LENGTH_SHORT).show();
        }

        //ROOM for messages
        if (messageDao.isUserExits(friendId).size() == 0) {
            getMessagesForFirstTime(friendId, recyclerView);
        } else { //user exits, just pull from data base.
            pullMessagesFromDao(friendId, recyclerView);
        }
    }

    private void continueOnCreateOnResponseLand(ArrayList<MessageResponse> allMessages, RecyclerView recyclerView, String friendId) {
        try {
            LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            messages = allMessages;
            ConversationAdapter adapter = new ConversationAdapter(messages, MainActivity.this, recyclerView);
            NotificationsService.conversationAdapter = adapter;
            recyclerView.setAdapter(adapter);

            Client.mainActivity = MainActivity.this;
            adapter.setMessages(messages);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);

            btnSendConv = findViewById(R.id.btnSendConvConv);
            btnSendConv.setOnClickListener(v -> sendMessage(friendId, adapter));

            EditText txtMsg = (EditText) findViewById(R.id.txtEnterMsgConv);
            txtMsg.setOnKeyListener((v, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage(friendId, adapter);
                    return true;
                }
                return false;
            });
        } catch(Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }

    private void pullMessagesFromDao(String friendId, RecyclerView recyclerView) {
        try {
            int chatId = messageDao.isUserExits(friendId).get(0).chatId;
            continueOnCreateOnResponseLand((ArrayList<MessageResponse>) messageDao.getMessagesByChatId(chatId), recyclerView, friendId);
        } catch (Exception ex) {
            Log.d("Dao", "user doesn't have messages");
            Call<ArrayList<MessageResponse>> allMessages = webServiceAPI.getMessagesById(friendId, Client.getToken());
            allMessages.enqueue(new Callback<ArrayList<MessageResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<MessageResponse>> call, Response<ArrayList<MessageResponse>> response) {
                    if (response.body() == null) {
                        return;
                    }
                    continueOnCreateOnResponseLand(response.body(), recyclerView, friendId);
                }

                @Override
                public void onFailure(Call<ArrayList<MessageResponse>> call, Throwable t) {
                    Log.d("Conversation", t.getMessage());
                }
            });
        }
    }

    private void getMessagesForFirstTime(String friendId, RecyclerView recyclerView) {
        Call<ArrayList<MessageResponse>> allMessages = webServiceAPI.getMessagesById(friendId, Client.getToken());
        allMessages.enqueue(new Callback<ArrayList<MessageResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MessageResponse>> call, Response<ArrayList<MessageResponse>> response) {
                if (response.body() == null) {
                    return;
                }

                messageDao.insertList(new ArrayList<>(response.body()));
                // get chatId
                try {
                    int chatId = messageDao.isUserExits(friendId).get(0).chatId;
                    continueOnCreateOnResponseLand((ArrayList<MessageResponse>) messageDao.getMessagesByChatId(chatId), recyclerView, friendId);
                } catch (Exception exception) {
                    Log.d("Dao", "user doesn't have messages");
                    continueOnCreateOnResponseLand(response.body(), recyclerView, friendId);
                }
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
            Client.setToken("");
            Client.setUserId("");
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void update(Contact c) {
        ImageView imageWelcome = findViewById(R.id.imgViewConv);
        if (imageWelcome == null)
            return;
        imageWelcome.setVisibility(View.GONE);

        RelativeLayout layout = findViewById(R.id.convRelativeLayout);
        if (layout == null) return;
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

    private void updateImagesInDB(ContactDao contactDao) throws IOException {
        List<String> AllIds = contactDao.getAllIds();
        for (String id :
                AllIds) {
            new AsyncTaskDao(contactDao, id).execute(contactDao.getURL(id));
        }
    }


}
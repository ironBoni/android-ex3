package com.ex3.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ex3.androidchat.adapters.ConversationAdapter;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;
import com.ex3.androidchat.database.MessageDB;
import com.ex3.androidchat.database.MessageDao;
import com.ex3.androidchat.events.IEventListener;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Utils;
import com.ex3.androidchat.models.contacts.GetUserDetailsResponse;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.ex3.androidchat.models.transfer.TransferRequest;
import com.ex3.androidchat.services.ChatService;
import com.ex3.androidchat.services.GetByAsyncTask;
import com.ex3.androidchat.services.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConversationActivity extends AppCompatActivity implements IEventListener<String> {
    ImageView backButton, btnSendConv;
    ChatService service = new ChatService();
    ConversationAdapter adapter;
    Chat conversation;
    int lastViewHeight;
    ArrayList<MessageResponse> messages;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    MessageDao messageDao;
    ContactDao contactDao;

    private final int DIFF = 100;
    private final int HEIGHT_RECYCLE_KEYBOARD_OPEN = 518;
    private final int HEIGHT_KEYBOARD_CHANGE = 538;

    private void sendMessageToForeignServer(String friendId, String msg, String hisServer) {
        hisServer = Utils.getAndroidServer(hisServer);
        Retrofit hisRetrofit = new Retrofit.Builder()
                .baseUrl(hisServer + getApplicationContext().getString(R.string.api_str))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WebServiceAPI hisServiceAPI = hisRetrofit.create(WebServiceAPI.class);

        if (Utils.getAndroidServer(hisServer).equals(Utils.getAndroidServer(Client.getMyServer())))
            return;
        Call<Void> call = hisServiceAPI.transfer(new TransferRequest(Client.getUserId(), friendId, msg));
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
                    sendMessageToForeignServer(friendId, msg, hisServer);
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
        EditText txtMsg = (EditText) findViewById(R.id.txtEnterMsg);
        String msg = txtMsg.getText().toString();
        if (msg.isEmpty()) return;

        contactDao.updateLast(msg, friendId);
        java.util.Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        contactDao.updateDate(formatter.format(date),friendId);
      
        adapter.addNewMessage(msg);
        txtMsg.setText("");

        int chatId;
        try {
            chatId = messageDao.isUserExits(friendId).get(0).chatId;
        } catch (Exception ex) {
            Chat chat = service.getChatByParticipants(friendId, Client.getUserId());
            if (chat != null) {
                chatId = chat.getId();
            }
            else {
                chatId = service.getAll().get(0).id;
            }
        }
        messageDao.insert(new MessageResponse(Client.getUserId(), msg, friendId, chatId));
        sendMessageToServer(friendId, msg);

        RecyclerView recyclerView = findViewById(R.id.messagesView);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void continueOnCreateOnResponse(ArrayList<MessageResponse> allMessages, RecyclerView recyclerView, String friendId) {

        messages = allMessages;
        adapter.setMessages(messages);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ConversationActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnSendConv = findViewById(R.id.btnSendConv);
        btnSendConv.setOnClickListener(v -> sendMessage(friendId, adapter));

        EditText txtMsg = (EditText) findViewById(R.id.txtEnterMsg);
        txtMsg.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                sendMessage(friendId, adapter);
                return true;
            }
            return false;
        });
    }

    private static ArrayList<MessageResponse> userMessages;

    private int toPixels(int dp) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Client.conversationActivity = ConversationActivity.this;
        messageDao = MessageDB.insert(this).messageDao();

        final View activityRootView = findViewById(R.id.mainConvlayout);
        lastViewHeight = findViewById(Window.ID_ANDROID_CONTENT).getHeight();

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            RelativeLayout.LayoutParams params;
            int currentContentHeight = findViewById(Window.ID_ANDROID_CONTENT).getHeight();
            RecyclerView recyclerView = findViewById(R.id.messagesView);
            if (recyclerView == null) return;

            // keyboard open
            if (lastViewHeight > currentContentHeight + DIFF) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, HEIGHT_RECYCLE_KEYBOARD_OPEN);
                recyclerView.setLayoutParams(params);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                lastViewHeight = currentContentHeight;
            } else if (currentContentHeight > lastViewHeight + DIFF) {
                // Keyboard is closed
                lastViewHeight = currentContentHeight;
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        toPixels(HEIGHT_KEYBOARD_CHANGE));
                recyclerView.setLayoutParams(params);
            }
        });

        getSupportActionBar().hide();
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        String friendId = getIntent().getStringExtra("id");
        Client.setFriendId(friendId);
        String nickname = getIntent().getStringExtra("nickname");
        String server = getIntent().getStringExtra("server");
        String image = getIntent().getStringExtra("image");

        TextView contactNickname = findViewById(R.id.contactNickname);
        if (contactNickname != null)
            contactNickname.setText(nickname);

        ImageView view = findViewById(R.id.user_image);
        new GetByAsyncTask((ImageView) view).execute(image);

        RecyclerView recyclerView = findViewById(R.id.messagesView);
        ConversationAdapter adapter = new ConversationAdapter(messages, this, recyclerView);
        NotificationsService.conversationAdapter = adapter;
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        this.adapter = adapter;
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        try {
            conversation = service.getChatByParticipants(Client.getUserId(), friendId);
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

    private void pullMessagesFromDao(String friendId, RecyclerView recyclerView) {
        try {
            int chatId = messageDao.isUserExits(friendId).get(0).chatId;
            continueOnCreateOnResponse((ArrayList<MessageResponse>) messageDao.getMessagesByChatId(chatId), recyclerView, friendId);
        } catch (Exception ex) {
            Log.d("Dao", "user doesn't have messages");
            Call<ArrayList<MessageResponse>> allMessages = webServiceAPI.getMessagesById(friendId, Client.getToken());
            allMessages.enqueue(new Callback<ArrayList<MessageResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<MessageResponse>> call, Response<ArrayList<MessageResponse>> response) {
                    if (response.body() == null) {
                        return;
                    }
                    continueOnCreateOnResponse(response.body(), recyclerView, friendId);
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
                    continueOnCreateOnResponse((ArrayList<MessageResponse>) messageDao.getMessagesByChatId(chatId), recyclerView, friendId);
                } catch (Exception exception) {
                    Log.d("Dao", "user doesn't have messages");
                    continueOnCreateOnResponse(response.body(), recyclerView, friendId);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MessageResponse>> call, Throwable t) {
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
package com.ex3.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.ex3.androidchat.events.IEventListener;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Utils;
import com.ex3.androidchat.models.contacts.GetUserDetailsResponse;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.ex3.androidchat.models.contacts.SendMessageRequest;
import com.ex3.androidchat.models.transfer.TransferRequest;
import com.ex3.androidchat.services.ChatService;
import com.ex3.androidchat.services.GetByAsyncTask;
import com.ex3.androidchat.services.UserService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConversationActivity extends AppCompatActivity implements IEventListener<String> {
    ImageView backButton, btnSendConv;
    ChatService service = new ChatService();
    ConversationAdapter adapter;
    Chat conversition;
    int lastViewHeight;
    ArrayList<MessageResponse> messages;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    private final int DIFF = 100;
    private final int HEIGHT_RECYCLE_KEYBOARD_OPEN = 518;

    private void sendMessageToForeignServer(String friendId, String msg, String hisServer) {
        hisServer = Utils.getAndroidServer(hisServer);
        Retrofit hisRetrofit = new Retrofit.Builder()
                .baseUrl(hisServer + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WebServiceAPI hisServiceAPI = hisRetrofit.create(WebServiceAPI.class);

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
        Call<Void> call = webServiceAPI.sendMessage(friendId, new SendMessageRequest(msg), Client.getToken());
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
        EditText txtMsg = (EditText) findViewById(R.id.txtEnterMsg);
        String msg = txtMsg.getText().toString();
        if (msg.isEmpty()) return;

        adapter.addNewMessage(msg);
        txtMsg.setText("");
        sendMessageToServer(friendId, msg);

        RecyclerView recyclerView = findViewById(R.id.messagesView);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void continueOnCreateOnResponse(ArrayList<MessageResponse> allMessages, RecyclerView recyclerView, String friendId) {
        messages = allMessages;
        adapter.setMessages(messages);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConversationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSendConv = findViewById(R.id.btnSendConv);
        btnSendConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(friendId, adapter);
            }
        });

        EditText txtMsg = (EditText) findViewById(R.id.txtEnterMsg);
        txtMsg.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage(friendId, adapter);
                    return true;
                }
                return false;
            }
        });

        /*txtMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                RelativeLayout.LayoutParams params;
                if (hasFocus) {
                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, toPixels(528));
                    recyclerView.setLayoutParams(params);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
                *//*else {
                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 528);
                    recyclerView.setLayoutParams(params);
                }*//*
            }
        });*/
    }

    private int toPixels(int dp) {

        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        final View activityRootView = findViewById(R.id.mainConvlayout);
        lastViewHeight = findViewById(Window.ID_ANDROID_CONTENT).getHeight();

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
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
                }
                else if (currentContentHeight > lastViewHeight + DIFF) {
                    // Keyboard is closed
                    lastViewHeight = currentContentHeight;
                    params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, toPixels(538));
                    recyclerView.setLayoutParams(params);
                }
            }
        });

        getSupportActionBar().hide();
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);


/*        RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainConvlayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = findViewById(R.id.messagesView);
                if (recyclerView == null) return;
                RelativeLayout.LayoutParams params;
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 528);
                recyclerView.setLayoutParams(params);
            }
        });*/
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
        ConversationAdapter adapter = new ConversationAdapter(messages, this);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        this.adapter = adapter;
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        try {
            conversition = service.GetChatByParticipants(Client.getUserId(), friendId);
        } catch (Exception ex) {
            Log.e("Conversation", ex.getMessage());
            Toast.makeText(this, "Contact could not be loaded.", Toast.LENGTH_SHORT).show();
        }
        //messages = conversition.getMessages();

        Call<ArrayList<MessageResponse>> allMessages = webServiceAPI.getMessagesById(friendId, Client.getToken());
        allMessages.enqueue(new Callback<ArrayList<MessageResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MessageResponse>> call, Response<ArrayList<MessageResponse>> response) {
                continueOnCreateOnResponse(response.body(), recyclerView, friendId);
            }

            @Override
            public void onFailure(Call<ArrayList<MessageResponse>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });
    }

    private void addNewMessage(String friendId, String text) {
        Chat conversation = service.GetChatByParticipants(Client.getUserId(), friendId);
        conversation.addMessage(text, Client.getUserId());
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
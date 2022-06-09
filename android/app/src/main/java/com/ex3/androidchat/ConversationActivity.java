package com.ex3.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ex3.androidchat.adapters.ConversationAdapter;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.databinding.ActivityConversationBinding;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.contacts.GetUserDetailsResponse;
import com.ex3.androidchat.models.contacts.MessageResponse;
import com.ex3.androidchat.models.contacts.SendMessageRequest;
import com.ex3.androidchat.models.transfer.TransferRequest;
import com.ex3.androidchat.services.ChatService;
import com.ex3.androidchat.services.GetByAsyncTask;
import com.ex3.androidchat.services.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConversationActivity extends AppCompatActivity {
    ActivityConversationBinding binding;
    ImageView backButton, btnSendConv;
    ChatService service =  new ChatService();
    Chat conversition;
    List<MessageResponse> messages;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;


    private void sendMessageToForeignServer(String friendId, String msg, String hisServer) {
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
                if(response.body() == null) {
                    Log.d("retrofit", "got empty body");
                    return;
                }
                String hisServer = response.body().server;
                UserService userService = new UserService();
                hisServer = userService.getFullServerUrl(hisServer);
                String myServer = Client.getMyServer();
                if(myServer.indexOf(hisServer) < 0 && hisServer.indexOf(myServer) < 0) {
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
        adapter.addNewMessage(msg);
        txtMsg.setText("");
        adapter.notifyDataSetChanged();
        sendMessageToServer(friendId, msg);
    }

    private void continueOnCreateOnResponse(List<MessageResponse> allMessages, RecyclerView recyclerView, String friendId) {
        messages = allMessages;
        ConversationAdapter adapter = new ConversationAdapter(messages,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        retrofit = new Retrofit.Builder()
                .baseUrl(getApplicationContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        String friendId =  getIntent().getStringExtra("id");
        Client.setFriendId(friendId);
        String nickname =  getIntent().getStringExtra("nickname");
        String server =  getIntent().getStringExtra("server");
        String image =  getIntent().getStringExtra("image");
        binding.contactNickname.setText(nickname);

        ImageView view = findViewById(R.id.user_image);
        new GetByAsyncTask((ImageView) view).execute(image);

        RecyclerView recyclerView = findViewById(R.id.messagesView);
        try {
            conversition = service.GetChatByParticipants(Client.getUserId(), friendId);
        } catch(Exception ex) {
            Log.e("Conversation", ex.getMessage());
            Toast.makeText(this, "Contact could not be loaded.", Toast.LENGTH_SHORT).show();
        }
        //messages = conversition.getMessages();

        Call<List<MessageResponse>> allMessages = webServiceAPI.getMessagesById(friendId, Client.getToken());
        allMessages.enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                continueOnCreateOnResponse(response.body(), recyclerView, friendId);
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
            }
        });
    }

    private void addNewMessage(String friendId, String text) {
        Chat conversation = service.GetChatByParticipants(Client.getUserId(), friendId);
        conversation.addMessage(text, Client.getUserId());
    }
}
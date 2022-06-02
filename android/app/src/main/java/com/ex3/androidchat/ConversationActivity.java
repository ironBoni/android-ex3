package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ex3.androidchat.adapters.ConversationAdapter;
import com.ex3.androidchat.databinding.ActivityConversationBinding;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Message;
import com.ex3.androidchat.services.ChatService;

import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity {
    ActivityConversationBinding binding;
    ImageView backButton;
    ChatService service =  new ChatService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConversationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        String id =  getIntent().getStringExtra("id");
        String nickname =  getIntent().getStringExtra("nickname");
        String server =  getIntent().getStringExtra("server");
        String image =  getIntent().getStringExtra("image");
//
        binding.contactNickname.setText(nickname);
//
        RecyclerView recyclerView = findViewById(R.id.messagesView);
        Chat conversition = service.GetChatByParticipants(Client.getUser(),nickname);
        ArrayList<Message> test = conversition.getMessages();
        ConversationAdapter adapter = new ConversationAdapter(conversition.getMessages(),this);
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
    }
}
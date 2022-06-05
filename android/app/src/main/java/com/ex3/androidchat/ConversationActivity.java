package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ex3.androidchat.adapters.ConversationAdapter;
import com.ex3.androidchat.databinding.ActivityConversationBinding;
import com.ex3.androidchat.models.Chat;
import com.ex3.androidchat.models.Message;
import com.ex3.androidchat.services.ChatService;
import com.ex3.androidchat.services.GetByAsyncTask;

import java.util.ArrayList;
import java.util.Arrays;

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
        binding.contactNickname.setText(nickname);

        ImageView view = findViewById(R.id.user_image);
        new GetByAsyncTask((ImageView) view).execute(image);

        RecyclerView recyclerView = findViewById(R.id.messagesView);
        Chat conversition;
        try {
            conversition = service.GetChatByParticipants(Client.getUserId(), id);
        } catch(Exception ex) {
            ArrayList<Message> messages = new ArrayList<Message>(Arrays.asList(
                    new Message(1, "text", "my name is " + nickname, id, "04.06.2021, 09:56:00", false),
                    new Message(2, "text", "my name is " + Client.getUser().getName(), Client.getUserId(), "04.06.2021 10:05:00", true),
                    new Message(3, "text", "Nice to meet you!", id, "04.08.2021 10:30:00", false)));

            ArrayList<String> participants = new ArrayList<>();
            participants.add(id);
            participants.add(Client.getUserId());
            service.Create(new Chat(participants, messages));
            conversition = service.GetChatByParticipants(Client.getUserId(), id);
        }
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
package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ex3.androidchat.databinding.ActivityConversationBinding;

public class ConversationActivity extends AppCompatActivity {
    ActivityConversationBinding binding;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        getSupportActionBar().hide();

        String id =  getIntent().getStringExtra("id");
        String nickname =  getIntent().getStringExtra("nickname");
        String server =  getIntent().getStringExtra("server");
        String image =  getIntent().getStringExtra("image");

        binding.contactNickname.setText(nickname);

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
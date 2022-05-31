package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ex3.androidchat.models.Contact;

public class AddUserActivity extends AppCompatActivity {
    Button cAdd;
    EditText cName, cNickname, cServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        cAdd = findViewById(R.id.cAdd);
        cName = findViewById(R.id.cName);
        cNickname = findViewById(R.id.cNickname);
        cServer = findViewById(R.id.cServer);

        cAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send name to server and get contact, then add it to list

            }
        });
    }

}
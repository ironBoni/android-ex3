package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ex3.androidchat.services.UserService;

public class SettingsActivity extends AppCompatActivity {
    Button btnSaveChanges;
    ImageView btnBack;
    EditText txtSettingsServer;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle(R.string.settings);

        userService = new UserService();

        btnBack = findViewById(R.id.btnBackSettings);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnSaveChanges = findViewById(R.id.btnSaveSettings);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSettingsServer = findViewById(R.id.txtSettingsServer);
                String server = txtSettingsServer.getText().toString();
                userService.getById(Client.getUserId()).setServer(server);
                String serverOfUser = userService.getById(Client.getUserId()).getServer();
                Toast.makeText(SettingsActivity.this, "Success: Your new server is " + serverOfUser, Toast.LENGTH_LONG).show();
            }
        });
    }
}
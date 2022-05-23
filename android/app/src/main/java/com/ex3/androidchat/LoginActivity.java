package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ex3.androidchat.databinding.ActivityLoginBinding;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button buttonRegister, buttonLogin;
    ActivityLoginBinding binding;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Sign in");
        dialog.setMessage("Please wait...\nLogin deatils checked");

        buttonRegister = findViewById(R.id.btnRegister);
        buttonLogin = findViewById(R.id.btnLogin);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.txtUserId.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter userId", Toast.LENGTH_LONG);
                } else if (binding.txtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_LONG);
                } else {
                    dialog.show();
                    Map<String, Object> body = new LinkedHashMap<>();
                    body.put("username", binding.txtUserId.toString());
                    body.put("password", binding.txtPassword.toString());
                    Response res = Client.sendPost("api/login/android", body);
                }
            }
        });
    }
}
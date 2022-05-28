package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ex3.androidchat.services.IUserService;
import com.ex3.androidchat.services.UserService;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button buttonRegister, buttonLogin;
    EditText txtUserId, txtPassword;
    ProgressDialog dialog;
    IUserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userService = new UserService();
        txtUserId = findViewById(R.id.txtUserId);
        txtPassword = findViewById(R.id.txtPassword);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Sign in");
        dialog.setMessage("Please wait...\nLogin deatils checked");

        buttonRegister = findViewById(R.id.btnSignup);
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
                txtUserId = findViewById(R.id.txtUserId);
                txtPassword = findViewById(R.id.txtPassword);
                if (txtUserId.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter userId", Toast.LENGTH_LONG);
                } else if (txtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_LONG);
                } else {
                    handleLogin();
                }
            }
        });

        if(Client.getToken() != "") {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void handleLogin() {
        dialog.show();
/*        Map<String, Object> body = new LinkedHashMap<>();
        body.put("username", txtUserId.getText().toString());
        body.put("password", txtPassword.getText().toString());*/
        //Response res = Client.sendPost("login/android", body);
        dialog.dismiss();
        this.userService = new UserService();
        boolean isLoginOk = userService.isLoginOk(txtUserId.getText().toString(), txtPassword.getText().toString());
        if(isLoginOk) {
            Client.setToken("abcd");
            Client.setUser(txtUserId.getText().toString());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Username or password are incorrect",
                    Toast.LENGTH_LONG).show();
        }
    }
}
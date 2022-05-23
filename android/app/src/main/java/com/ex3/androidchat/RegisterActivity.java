package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;

import java.io.*;
import java.net.*;
import java.util.*;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ex3.androidchat.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setTitle("Register");
        dialog.setMessage("Creating new account for you.");
        Button button = (Button) findViewById(R.id.btnRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isValid = true;
                if (!hasEnteredAllFields()) return;
                if (!isValidUser(binding.txtUserId.toString())) return;
                if (!isValidPassword(binding.txtPassword.toString())) return;
                if (!isValidConfirmPassword(binding.txtPassword.toString(),
                        binding.txtConfirm.toString())) return;
                createUserInServer();
            }

            private void createUserInServer() {

            }

            private boolean isValidConfirmPassword(String password, String confirmPassword) {
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            private boolean isValidPassword(String password) {
                if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters",
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                Pattern pattern = Pattern.compile("^.*(?=.*\\d)(?=.*[A-Z])(?=.*[1-9]).*$");
                Matcher matcher = pattern.matcher(password);
                boolean isOk = matcher.find();
                if (!isOk) {
                    Toast.makeText(RegisterActivity.this, "Password must contain " +
                            "digits and at least one capital letter", Toast.LENGTH_LONG).show();
                }
                return isOk;
            }

            private boolean isValidUser(String userId) {
                if (userId != "noam") {
                    Toast.makeText(RegisterActivity.this, "User already exists!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (userId.indexOf(' ') >= 0) {
                    Toast.makeText(RegisterActivity.this, "User cannot have spaces!", Toast.LENGTH_LONG).show();
                    return false;
                }

                return true;
            }

            private boolean hasEnteredAllFields() {
                if (binding.txtUserId.toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter User Id!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (binding.txtNickname.toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Nickname!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (binding.txtPassword.toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Password!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (binding.txtConfirm.toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Confirm Password!", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
    }
}
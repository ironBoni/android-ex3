package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ex3.androidchat.databinding.ActivityRegisterBinding;
import com.ex3.androidchat.models.User;

public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    EditText txtUserId, txtPassword, txtNickname, txtConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtUserId = findViewById(R.id.txtUserId);
        txtPassword = findViewById(R.id.txtPassword);
        txtNickname = findViewById(R.id.txtNickname);
        txtConfirm = findViewById(R.id.txtConfirm);
        setContentView(R.layout.activity_register);
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
                if (!isValidUser(txtUserId.getText().toString())) return;
                if (!isValidPassword(txtPassword.getText().toString())) return;
                if (!isValidConfirmPassword(txtPassword.getText().toString(),
                        txtConfirm.getText().toString())) return;
                createUserInServer();
            }

            private void createUserInServer() {
                dialog.show();
                Map<String, Object> body = new LinkedHashMap<>();
                body.put("id", txtUserId.getText().toString());
                body.put("name", txtNickname.getText().toString());
                body.put("password", txtPassword.getText().toString());
                body.put("profileImage", "/images/default.jpg");
                body.put("server", Client.getMyServer());
                Response res = Client.sendPost("api/Register", body);
                Log.d("Register", "success " + res.getResponse() + " " + res.getStatus());
                if(res.getStatus() == 200) {
                    User user = new User(txtUserId.getText().toString(), txtPassword.getText().toString(),
                            txtNickname.getText().toString(), Client.getMyServer());
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                 else
                     Toast.makeText(RegisterActivity.this, "error in register", Toast.LENGTH_SHORT);
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
                if (txtUserId.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter User Id!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txtNickname.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Nickname!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Password!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txtConfirm.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Confirm Password!", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
    }
}
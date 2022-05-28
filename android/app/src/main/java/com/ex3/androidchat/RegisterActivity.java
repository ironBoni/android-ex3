package com.ex3.androidchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ex3.androidchat.databinding.ActivityRegisterBinding;
import com.ex3.androidchat.models.User;
import com.ex3.androidchat.services.IUserService;
import com.ex3.androidchat.services.UserService;

public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    IUserService userService;
    EditText txtUserIdR, txtPasswordR, txtNickname, txtConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserService();
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setTitle("Register");
        dialog.setMessage("Creating new account for you.");
        Button button = (Button) findViewById(R.id.btnRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtUserIdR = findViewById(R.id.txtUserIdR);
                txtPasswordR = findViewById(R.id.txtPasswordR);
                txtNickname = findViewById(R.id.txtNickname);
                txtConfirm = findViewById(R.id.txtConfirm);
                if (!hasEnteredAllFields()) return;
                if (!isValidUser(txtUserIdR.getText().toString())) return;
                if (!isValidPassword(txtPasswordR.getText().toString())) return;
                if (!isValidConfirmPassword(txtPasswordR.getText().toString(),
                        txtConfirm.getText().toString())) return;
                createUser();
            }

            private void createUser() {
                dialog.show();
                userService = new UserService();
                txtUserIdR = findViewById(R.id.txtUserIdR);
                txtPasswordR = findViewById(R.id.txtPasswordR);
                txtNickname = findViewById(R.id.txtNickname);
                txtConfirm = findViewById(R.id.txtConfirm);
                User user = new User(txtUserIdR.getText().toString(), txtPasswordR.getText().toString(),
                        txtNickname.getText().toString(), Client.getMyServer());
                boolean isRegisterOk = userService.create(user);
                if (isRegisterOk) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else
                    Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterActivity.this, "Password must contain 6 characters: both" +
                            " digits and letters and at least one capital letter", Toast.LENGTH_LONG).show();
                }
                return isOk;
            }

            private boolean isValidUser(String userId) {
                if (userService.getById(userId) != null) {
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
                txtUserIdR = findViewById(R.id.txtUserIdR);
                txtPasswordR = findViewById(R.id.txtPasswordR);
                txtNickname = findViewById(R.id.txtNickname);
                txtConfirm = findViewById(R.id.txtConfirm);
                if (txtUserIdR.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter User Id!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txtNickname.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Nickname!", Toast.LENGTH_LONG).show();
                    return false;
                }
                if (txtPasswordR.getText().toString().isEmpty()) {
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
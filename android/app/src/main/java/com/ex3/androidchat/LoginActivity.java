package com.ex3.androidchat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;
import com.ex3.androidchat.database.MessageDB;
import com.ex3.androidchat.database.MessageDao;
import com.ex3.androidchat.events.IEventListener;
import com.ex3.androidchat.models.contacts.UserModel;
import com.ex3.androidchat.models.login.LoginRequest;
import com.ex3.androidchat.models.login.LoginResponse;
import com.ex3.androidchat.models.login.TokenResponse;
import com.ex3.androidchat.services.IUserService;
import com.ex3.androidchat.services.UserService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements IEventListener<String> {
    Button buttonRegister, buttonLogin;
    EditText txtUserId, txtPassword;
    ProgressDialog dialog;
    IUserService userService;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    MessageDao messageDao;
    ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

/*
        contactDao = AppDB.getContactDBInstance(this).contactDao();
        contactDao.deleteAll();
        messageDao = MessageDB.insert(this).messageDao();
        messageDao.deleteAll();
        Log.d("Deleted", "Deleted two rooms");
*/

        AndroidChat.context = getApplicationContext();
        this.userService = new UserService();
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        txtUserId = findViewById(R.id.txtUserId);
        txtPassword = findViewById(R.id.txtPassword);

        int currentNightMode = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (txtUserId == null || txtPassword == null) return;
            txtUserId.setTypeface(null, Typeface.BOLD);
            txtPassword.setTypeface(null, Typeface.BOLD);
        }
        txtPassword.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                doLogin();
                return true;
            }
            return false;
        });
        getSupportActionBar().hide();

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle(getString(R.string.sign_in));
        dialog.setMessage(getString(R.string.please_wait));

        buttonRegister = findViewById(R.id.btnSignup);
        buttonLogin = findViewById(R.id.btnLogin);
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        buttonLogin.setOnClickListener(v -> doLogin());

        if (Client.getToken() != "") {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void doLogin() {
        txtUserId = findViewById(R.id.txtUserId);
        txtPassword = findViewById(R.id.txtPassword);
        if (txtUserId.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, getApplicationContext().getString(R.string.enter_user_id), Toast.LENGTH_LONG).show();
        } else if (txtPassword.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, getApplicationContext().getString(R.string.enter_password), Toast.LENGTH_LONG).show();
        } else {
            handleLogin();
        }
    }

    private void initNotificationsService() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, instanceIdResult -> {
            Client.firebaseToken = instanceIdResult.getToken();
            if (Client.firebaseToken == "") return;
            Thread t = new Thread(() -> {
                Call<Void> call = webServiceAPI.setTokenForPush(new TokenResponse(Client.firebaseToken),
                        Client.getToken());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(getApplicationContext().getString(R.string.firebase), "set token in server");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t1) {
                        Log.e(getApplicationContext().getString(R.string.firebase), t1.getMessage());
                    }
                });
            });
            t.run();
        });
    }

    private void setUsers() {
        Call<ArrayList<UserModel>> call = webServiceAPI.getUsers(Client.getToken());
        call.enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                if (response.body() == null) return;

                UserService.setUsers(new ArrayList<>(response.body()));
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                Log.e(getApplicationContext().getString(R.string.retrofit), t.getMessage());
            }
        });
    }

    private void handleLogin() {
        dialog.show();
        dialog.dismiss();
        this.userService = new UserService();
        Call<LoginResponse> call = webServiceAPI.login(new LoginRequest(txtUserId.getText().toString(), txtPassword.getText().toString()));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().isCorrectInput()) {
                    String token = response.body().getToken().getToken();
                    Client.setToken("Bearer " + token);
                    initNotificationsService();

                    Client.setUserId(txtUserId.getText().toString());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    setUsers();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Username or password are incorrect",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Username or password are incorrect",
                        Toast.LENGTH_LONG).show();

                Log.e("retrofit", t.getMessage());
            }
        });
    }

    @Override
    public void update(String element) {
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
}
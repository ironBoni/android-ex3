package com.ex3.androidchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.events.IEventListener;
import com.ex3.androidchat.models.contacts.UserModel;
import com.ex3.androidchat.models.login.LoginRequest;
import com.ex3.androidchat.models.login.LoginResponse;
import com.ex3.androidchat.models.login.TokenResponse;
import com.ex3.androidchat.services.IUserService;
import com.ex3.androidchat.services.UserService;
import com.google.firebase.iid.FirebaseInstanceId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initNotificationsService();
        AndroidChat.context = getApplicationContext();
        this.userService = new UserService();
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        txtUserId = findViewById(R.id.txtUserId);
        txtPassword = findViewById(R.id.txtPassword);

        txtPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    doLogin();
                    return true;
                }
                return false;
            }
        });
        getSupportActionBar().hide();

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Sign in");
        dialog.setMessage("Please wait...\nLogin details checked");

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
                doLogin();
            }
        });

        if(Client.getToken() != "") {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void doLogin() {
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

    private void initNotificationsService() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, instanceIdResult -> {
            Client.firebaseToken =  instanceIdResult.getToken();
            if(Client.firebaseToken == "") return;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Call <Void> call = webServiceAPI.setTokenForPush(new TokenResponse(Client.firebaseToken),
                            Client.getToken());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("firebase", "set token in server");
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("firebase", t.getMessage());
                        }
                    });
                }
            });
            t.run();
        });
    }

    private void setUsers() {
        Call<ArrayList<UserModel>> call = webServiceAPI.getUsers(Client.getToken());
        call.enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                if(response.body() == null) return;

                UserService.setUsers(new ArrayList<>(response.body()));
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                Log.e("retrofit", t.getMessage());
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
                if(response.body().isCorrectInput()) {
                    String token = response.body().getToken().getToken();
                    Client.setToken("Bearer " + token);
                    initNotificationsService();

                    Client.setUserId(txtUserId.getText().toString());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    setUsers();
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
        //boolean isLoginOk = userService.isLoginOk(txtUserId.getText().toString(), txtPassword.getText().toString());
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
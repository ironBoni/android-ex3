package com.ex3.androidchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.models.settings.ChangeServerRequest;
import com.ex3.androidchat.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity extends AppCompatActivity {
    Button btnSaveChanges;
    ImageView btnBack;
    EditText txtSettingsServer;
    UserService userService;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AndroidChat.context = getApplicationContext();
        retrofit = new Retrofit.Builder()
                .baseUrl(getApplicationContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

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

                Call<Void> call = webServiceAPI.changeSettings(new ChangeServerRequest(server), Client.getToken());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(SettingsActivity.this, "Success: Your new server is " + server, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("retrofit", t.getMessage());
                    }
                });
            }
        });
    }
}
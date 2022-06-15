package com.ex3.androidchat;

import android.app.ProgressDialog;
import android.app.UiModeManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.events.IEventListener;
import com.ex3.androidchat.models.Utils;
import com.ex3.androidchat.models.settings.ChangeServerRequest;
import com.ex3.androidchat.services.UserService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    Button btnSaveChanges, btnChangeTheme;
    EditText txtSettingsServer;
    UserService userService;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    private UiModeManager uiModeManager;
    private ArrayList<IEventListener<String>> listeners;


    // update listeners that the server has been changed.
    public void addListener(IEventListener<String> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (IEventListener<String> listener : listeners) {
            listener.update(Client.getMyServer());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        listeners = new ArrayList<>();
        setContentView(R.layout.activity_settings);
        dialog = new ProgressDialog(SettingsActivity.this);
        dialog.setTitle("Settings");
        dialog.setMessage("Saving the settings...");

        AndroidChat.context = getApplicationContext();
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        btnChangeTheme = findViewById(R.id.btnChangeTheme);
        btnChangeTheme.setOnClickListener(v -> {
            int currentNightMode = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_NO ||
                    currentNightMode == Configuration.UI_MODE_TYPE_UNDEFINED)
                turnOnNightMode();
            else {
                uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                Toast.makeText(SettingsActivity.this, "night mode is OFF!", Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().setTitle(R.string.settings);

        userService = new UserService();

        btnSaveChanges = findViewById(R.id.btnSaveSettings);
        btnSaveChanges.setOnClickListener(v -> {
            dialog.show();
            txtSettingsServer = findViewById(R.id.txtSettingsServer);
            String txtServerContent = txtSettingsServer.getText().toString();
            if(txtServerContent.equals("")) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                return;
            }
            String server = Utils.getFullServerUrl(txtSettingsServer.getText().toString());
            server = Utils.getAndroidServer(server);
            userService.getById(Client.getUserId()).setServer(server);

            Client.setMyServer(server);
            notifyListeners();
            Call<Void> call = webServiceAPI.changeSettings(new ChangeServerRequest(server), Client.getToken());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("retrofit", t.getMessage());
                }
            });
        });
    }

    private void turnOnNightMode() {
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        Toast.makeText(SettingsActivity.this, "night mode is ON!", Toast.LENGTH_SHORT).show();
    }
}
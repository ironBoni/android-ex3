package com.ex3.androidchat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;
import com.ex3.androidchat.models.Contact;

public class AddUserActivity extends AppCompatActivity {
    Button cAdd;
    EditText cName, cNickname, cServer;
    ContactDao contactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        AppDB db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "ContactDB")
                .allowMainThreadQueries()
                .build();

        contactDao = db.contactDao();

        cAdd = findViewById(R.id.cAdd);
        cName = findViewById(R.id.cName);
        cNickname = findViewById(R.id.cNickname);
        cServer = findViewById(R.id.cServer);

        cAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send name to server and get contact, then add it to list
                contactDao.insert(new Contact(cName.getText().toString(),
                        cNickname.getText().toString(),
                        cServer.getText().toString()));
            }
        });
    }

}
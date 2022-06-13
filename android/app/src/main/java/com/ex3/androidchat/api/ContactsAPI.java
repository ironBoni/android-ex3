package com.ex3.androidchat.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ex3.androidchat.Client;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.database.AppDB;
import com.ex3.androidchat.database.ContactDao;
import com.ex3.androidchat.models.Contact;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsAPI {
    private MutableLiveData<List<Contact>> contactListData;
    private ContactDao contactDao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    ArrayList<Contact> contacts = new ArrayList<>();

    //MutableLiveData<List<Contact>> postListData, ContactDao dao
    public ContactsAPI() {
        /*this.postListData = postListData;
        this.dao = dao;*/
        retrofit = new Retrofit.Builder()
                .baseUrl(Client.getMyServer())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public List<Contact> get() {
        Call<List<Contact>> call = webServiceAPI.getContacts(Client.getToken());
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                contacts = new ArrayList<>(response.body());
            }
            @Override public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.e("Retrofit", t.getMessage());
            }
    });

        return contacts;
    }
}
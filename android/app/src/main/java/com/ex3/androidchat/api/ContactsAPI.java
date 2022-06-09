package com.ex3.androidchat.api;

import android.util.Log;

import com.ex3.androidchat.AndroidChat;
import com.ex3.androidchat.R;
import com.ex3.androidchat.api.interfaces.WebServiceAPI;
import com.ex3.androidchat.models.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsAPI {
    //private MutableLiveData<List<Post>> postListData;
    //private PostDao dao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    // MutableLiveData<List<Post>> postListData, PostDao dao
    public ContactsAPI() {
        /*this.postListData = postListData;
        this.dao = dao;*/
        retrofit = new Retrofit.Builder()
                .baseUrl(AndroidChat.context.getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void get() {
        Call<List<Contact>> call = webServiceAPI.getContacts();
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                List<Contact> contacts = response.body();
            /*new Thread(() -> {
            dao.clear();
            dao.insertList(response.body());
            postListData.postValue(dao.get());
            }).start();*/
            //response.body();
     }

            @Override public void onFailure(Call<List<Contact>> call, Throwable t) {
                Log.e("Retrofit", t.getMessage());
            }
    });
    }
}
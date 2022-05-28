package com.ex3.androidchat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ex3.androidchat.Client;
import com.ex3.androidchat.R;
import com.ex3.androidchat.Response;
import com.ex3.androidchat.adapters.ContactsAdapter;
import com.ex3.androidchat.databinding.FragmentChatBinding;
import com.ex3.androidchat.models.Contact;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;

    public ChatFragment() {
        // Required empty public constructor
    }

    ArrayList<Contact> contacts = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ContactsAdapter uAdapter = new ContactsAdapter(contacts, getContext());
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        binding.recycleView.setAdapter(uAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.recycleView.setLayoutManager(manager);

        /*Response res = Client.sendGet("contacts");
        ObjectInputStream stream;
        try {
            stream = new ObjectInputStream(new ByteArrayInputStream(res.getResponse()));
            contacts = (ArrayList<Contact>) stream.readObject();
            stream.close();
        } catch(Exception ex) {
            Log.d("ChatFragment", ex.toString());
        }*/
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }
}
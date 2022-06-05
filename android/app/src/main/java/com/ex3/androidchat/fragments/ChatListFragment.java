package com.ex3.androidchat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ex3.androidchat.Client;
import com.ex3.androidchat.adapters.ContactsAdapter;
import com.ex3.androidchat.databinding.FragmentChatListBinding;
import com.ex3.androidchat.models.Contact;
import com.ex3.androidchat.services.IUserService;
import com.ex3.androidchat.services.UserService;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {

    public ChatListFragment() {
        // Required empty public constructor
    }

    FragmentChatListBinding binding;
    IUserService service;
    ArrayList<Contact> contacts = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatListBinding.inflate(inflater, container, false);
        service = new UserService();
        contacts = service.getById(Client.getUserId()).getContacts();
        ContactsAdapter adapter = new ContactsAdapter(contacts, getContext());
        binding.chatListRecyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.chatListRecyclerView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();
        return binding.getRoot();
    }
}
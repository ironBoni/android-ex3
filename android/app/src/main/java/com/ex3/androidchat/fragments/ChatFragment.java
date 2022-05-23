package com.ex3.androidchat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ex3.androidchat.R;
import com.ex3.androidchat.adapters.UserAdapter;
import com.ex3.androidchat.models.User;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    public ChatFragment() {
        // Required empty public constructor
    }

    ArrayList<User> users = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        UserAdapter uAdapter = new UserAdapter(users, getContext());
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }
}
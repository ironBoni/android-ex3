package com.ex3.androidchat.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ex3.androidchat.fragments.ConversationFragment;

public class FragAdapter extends FragmentPagerAdapter {
    public FragAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // if(position == 0)
        return new ConversationFragment();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String head = "Chat";
        //(if position == 0) head = chat
        return head;
    }
}

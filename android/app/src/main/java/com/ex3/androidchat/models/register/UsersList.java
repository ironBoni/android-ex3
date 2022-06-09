package com.ex3.androidchat.models.register;

import java.util.List;

public class UsersList {
    public List<String> fullList;

    public UsersList() {
    }

    public UsersList(List<String> fullList) {
        this.fullList = fullList;
    }

    public List<String> getFullList() {
        return fullList;
    }

    public void setFullList(List<String> fullList) {
        this.fullList = fullList;
    }
}

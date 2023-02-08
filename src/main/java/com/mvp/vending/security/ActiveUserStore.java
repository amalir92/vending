package com.mvp.vending.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


public class ActiveUserStore {

    private List<String> activeUsers;

    @Autowired
    public void setActiveUsers(List<String> activeUsers) {
        this.activeUsers = activeUsers;
    }
    public void printUsers() {
        System.out.println(activeUsers);
    }

    public List<String> getActiveUsers()
    {
        return activeUsers;
    }
}

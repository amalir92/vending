package com.mvp.vending.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mvp.vending.security.ActiveUserStore;

@Configuration
public class ActiveUsersConfig {
    
    @Bean
    public ActiveUserStore getActiveUserStore() {
        return new ActiveUserStore();
    }

    @Bean
    public List<String> activeUsers(){
    List<String>  activeUsers = new ArrayList<>();
    activeUsers.add("John");
    activeUsers.add("Adam");
    activeUsers.add("Harry");
    return activeUsers;
}
}
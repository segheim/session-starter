package com.example.usertest.config;

import com.example.starter.service.BlackListProvider;

import java.util.Set;

public class UserBlackListProvider implements BlackListProvider {

    @Override
    public Set<String> getBlackList() {
        return Set.of("Lily", "Kenny");
    }
}

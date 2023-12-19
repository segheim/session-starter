package com.example.starter.service;

import com.example.starter.properties.SessionProviderProperties;
import lombok.Data;

import java.util.Set;

@Data
public class BlackListProviderProperties implements BlackListProvider {

    private final SessionProviderProperties sessionProviderProperties;

    @Override
    public Set<String> getBlackList() {
        return sessionProviderProperties.getBlackList();
    }
}

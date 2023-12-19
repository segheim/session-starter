package com.example.starter.properties;

import com.example.starter.service.BlackListProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "session.provider")
public class SessionProviderProperties {

    private String url;
    private Boolean enable;
    private Set<String> blackList = new HashSet<>();
    private Set<? extends BlackListProvider> blackListProviders = new HashSet<>();

}

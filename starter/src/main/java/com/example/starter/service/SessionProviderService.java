package com.example.starter.service;

import com.example.starter.exception.SessionServiceException;
import com.example.starter.model.Session;
import com.example.starter.properties.SessionProviderProperties;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@AllArgsConstructor
public class SessionProviderService {

    private RestClient restClient;
    private SessionProviderProperties sessionProviderProperties;

    public Session getSession(String login) {
        try {
            return restClient.get()
                    .uri(sessionProviderProperties.getUrl() + "?login=" + login)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(Session.class);
        } catch (Exception e) {
            throw new SessionServiceException("Sorry, Something happened with Session Service");
        }
    }
}

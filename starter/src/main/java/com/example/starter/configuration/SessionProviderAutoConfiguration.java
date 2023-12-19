package com.example.starter.configuration;

import com.example.starter.bpp.SessionProviderBeanPostProcessor;
import com.example.starter.properties.SessionProviderProperties;
import com.example.starter.service.BlackListProvider;
import com.example.starter.service.BlackListProviderProperties;
import com.example.starter.service.SessionProviderService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(SessionProviderProperties.class)
@ConditionalOnProperty(prefix = "session.provider", name = "enable", havingValue = "true")
public class SessionProviderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RestClient.class)
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public SessionProviderBeanPostProcessor sessionProviderBeanPostProcessor() {
        return new SessionProviderBeanPostProcessor();
    }

    @Bean
    public BlackListProvider blackListProviderProperties(SessionProviderProperties sessionProviderProperties) {
        return new BlackListProviderProperties(sessionProviderProperties);
    }

    @Bean
    @ConditionalOnMissingBean(SessionProviderService.class)
    public SessionProviderService sessionProviderService(RestClient restClient, SessionProviderProperties sessionProviderProperties) {
        return new SessionProviderService(restClient, sessionProviderProperties);
    }
}

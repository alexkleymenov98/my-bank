package com.example.cash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient customRestClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        OAuth2ClientHttpRequestInterceptor oauth2 =
                new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
        oauth2.setClientRegistrationIdResolver(req -> "keycloak");
        return RestClient.builder()
                .requestInterceptor(oauth2)
                .build();
    }
}
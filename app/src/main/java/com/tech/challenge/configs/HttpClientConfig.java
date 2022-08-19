package com.tech.challenge.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFlux
public class HttpClientConfig {
    @Bean
    public WebClient getWebClient() {
        return WebClient
            .builder()
            .clientConnector(new ReactorClientHttpConnector())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
}

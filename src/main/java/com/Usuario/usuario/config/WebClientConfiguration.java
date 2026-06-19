package com.Usuario.usuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import java.util.Base64;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient.Builder webClientBuilder() {
        String credentials = Base64.getEncoder()
                .encodeToString("admin:admin123".getBytes());

        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + credentials);
    }
}

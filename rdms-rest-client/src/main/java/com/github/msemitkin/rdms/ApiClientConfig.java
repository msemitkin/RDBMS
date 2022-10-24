package com.github.msemitkin.rdms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ua.knu.csc.it.rdms.ApiClient;
import ua.knu.csc.it.rdms.api.DatabasesApi;
import ua.knu.csc.it.rdms.api.TablesApi;

@Configuration
public class ApiClientConfig {
    @Bean
    public WebClient webClient(@Value("${rdms.server.baseUrl}") String baseUrl) {
        return WebClient.create(baseUrl);
    }

    @Bean
    public ApiClient apiClient(
        WebClient webClient,
        @Value("${rdms.server.baseUrl}") String baseUrl
    ) {
        return new ApiClient(webClient)
            .setBasePath(baseUrl);
    }

    @Bean
    public DatabasesApi databasesApi(ApiClient apiClient) {
        return new DatabasesApi(apiClient);
    }

    @Bean
    public TablesApi tablesApi(ApiClient apiClient) {
        return new TablesApi(apiClient);
    }
}

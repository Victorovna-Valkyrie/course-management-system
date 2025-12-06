package com.valkyrie.enrollment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    @Value("${catalog-service.url:http://catalog-service:8081}")
    private String catalogServiceUrl;

    public String getCatalogServiceUrl() {
        return catalogServiceUrl;
    }
}
package com.master.dataloader.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.externalapi")
@Component
public class ApiProperties {

    private static ApiProperties instance;

    @Value("${app.externalapi.token}")
    private String token;

    @PostConstruct
    private void init() {
        instance = this;
    }

    public static ApiProperties getInstance() {
        return instance;
    }

    public String getToken() {
        return token;
    }
}

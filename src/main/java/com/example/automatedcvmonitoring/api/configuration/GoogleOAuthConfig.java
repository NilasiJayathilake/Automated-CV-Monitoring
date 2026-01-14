package com.example.automatedcvmonitoring.api.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "google.oauth")
public class GoogleOAuthConfig {
    private String clientSecretsFile = "classpath:google-secret.json";
    private String redirectUri;
    private String successRedirectUri = "/api/v1/gmail/poll"; // Default to poll page
    private List<String> scopes = List.of(
            "https://www.googleapis.com/auth/gmail.readonly",
            "https://www.googleapis.com/auth/gmail.modify",
            "https://www.googleapis.com/auth/gmail.labels"
    );
    private String downloadsDir;
}


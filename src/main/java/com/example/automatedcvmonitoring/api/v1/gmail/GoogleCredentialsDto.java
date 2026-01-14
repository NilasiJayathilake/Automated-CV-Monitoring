package com.example.automatedcvmonitoring.api.v1.gmail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleCredentialsDto {
    private String token;
    private String refreshToken;
    private String tokenUri;
    private String clientId;
    private String clientSecret;
    private List<String> scopes;
}


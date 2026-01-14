package com.example.automatedcvmonitoring.application.gmail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GmailMessageDto {
    private String subject;
    private String body;
    @Builder.Default
    private List<String> attachments = new ArrayList<>();
}



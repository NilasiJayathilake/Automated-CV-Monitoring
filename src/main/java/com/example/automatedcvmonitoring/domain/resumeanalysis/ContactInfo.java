package com.example.automatedcvmonitoring.domain.resumeanalysis;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactInfo {
    private String email;
    private String phone;
    private String linkedin;
}

package com.example.automatedcvmonitoring.domain.resumeanalysis;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public  class ProfileAssessment {
    private List<String> keyStrengths;
    private String profileSummary;
}
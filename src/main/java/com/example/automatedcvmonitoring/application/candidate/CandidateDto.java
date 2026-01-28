package com.example.automatedcvmonitoring.application.candidate;

import com.example.automatedcvmonitoring.domain.candidates.Candidate;
import com.example.automatedcvmonitoring.domain.candidates.ContactInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateDto {
    private String candidateName;
    private ContactInfo contactInfo;

    public static CandidateDto fromEntity(Candidate candidate) {
        return CandidateDto.builder()
                .candidateName(candidate.getCandidateName())
                .contactInfo(candidate.getContactInfo())
                .build();
    }

    public Candidate toEntity() {
        return Candidate.builder()
                .candidateName(candidateName)
                .contactInfo(contactInfo)
                .build();
    }
}

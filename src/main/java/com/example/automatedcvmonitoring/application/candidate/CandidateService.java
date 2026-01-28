package com.example.automatedcvmonitoring.application.candidate;

import com.example.automatedcvmonitoring.domain.candidates.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;

    public CandidateDto getCandidateById(String candidateId) {
        return candidateRepository.findById(candidateId)
                .map(CandidateDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found for candidateId: " + candidateId));
    }

}

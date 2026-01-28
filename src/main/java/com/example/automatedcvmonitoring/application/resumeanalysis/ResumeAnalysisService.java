package com.example.automatedcvmonitoring.application.resumeanalysis;

import com.example.automatedcvmonitoring.application.agents.ResumeAnalysisCompletion;
import com.example.automatedcvmonitoring.application.agents.ResumeAnalysisGenerator;
import com.example.automatedcvmonitoring.application.candidate.CandidateDto;
import com.example.automatedcvmonitoring.domain.candidates.Candidate;
import com.example.automatedcvmonitoring.domain.candidates.CandidateRepository;
import com.example.automatedcvmonitoring.domain.resumeanalysis.ResumeAnalysisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeAnalysisService {
    private final ResumeAnalysisGenerator resumeAnalysisGenerator;
    private final CandidateRepository candidateRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;

    public String analyzeResume(){
        try {
            ResumeAnalysisCompletion resumeAnalysis = resumeAnalysisGenerator.buildResumeAnalysis();
            CandidateDto candidateDto = resumeAnalysis.toCandidateDto();
            Candidate candidate = candidateRepository.save(candidateDto.toEntity());
            String candidateId = candidate.getId();
            ResumeAnalysisDto resumeAnalysisDto = resumeAnalysis.toDto(candidateId);
            resumeAnalysisRepository.save(resumeAnalysisDto.toEntity());
            log.info("Resume analysis completed for {}", candidateId);
            return candidateId;

        } catch (IOException e) {
            throw new RuntimeException("Unable to find any new CVs to Analyze", e);
        }
    }

    public ResumeAnalysisDto fetchAnalysis(String candidateId) {
        return resumeAnalysisRepository.findByCandidateId(candidateId)
                .map(ResumeAnalysisDto::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Resume analysis not found for candidateId: " + candidateId));
    }

}

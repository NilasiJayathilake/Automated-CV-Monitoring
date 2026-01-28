package com.example.automatedcvmonitoring.api.v1.resumeanalysis;

import com.example.automatedcvmonitoring.application.candidate.CandidateDto;
import com.example.automatedcvmonitoring.application.candidate.CandidateService;
import com.example.automatedcvmonitoring.application.resumeanalysis.ResumeAnalysisDto;
import com.example.automatedcvmonitoring.application.resumeanalysis.ResumeAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeAnalysisController {

    private final ResumeAnalysisService resumeAnalysisService;
    private final CandidateService candidateService;

    @PostMapping("/analyze")
    public ResponseEntity<ResumeAnalysisResponse> analyzeLatestResume() {
            String candidateId = resumeAnalysisService.analyzeResume();
            CandidateDto candidateDto = candidateService.getCandidateById(candidateId);
            ResumeAnalysisDto analysisDto = resumeAnalysisService.fetchAnalysis(candidateId);

            return ResponseEntity.ok(
                    ResumeAnalysisResponse.fromDto(analysisDto, candidateDto)
            );

    }
}

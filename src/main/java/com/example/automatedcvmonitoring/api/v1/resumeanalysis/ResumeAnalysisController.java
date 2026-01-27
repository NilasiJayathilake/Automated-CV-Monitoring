package com.example.automatedcvmonitoring.api.v1.resumeanalysis;

import com.example.automatedcvmonitoring.application.resumeanalysis.ResumeAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeLatestResume() {
        try {
            resumeAnalysisService.analyzeResume();
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Resume analysis Completed for the latest CV.");
        } catch (RuntimeException ex) {
            log.warn("Resume analysis failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error during resume analysis", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error during resume analysis");
        }
    }
}

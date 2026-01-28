package com.example.automatedcvmonitoring.api.v1.resumeanalysis;

import com.example.automatedcvmonitoring.application.candidate.CandidateDto;
import com.example.automatedcvmonitoring.application.resumeanalysis.ResumeAnalysisDto;
import com.example.automatedcvmonitoring.domain.candidates.ContactInfo;
import com.example.automatedcvmonitoring.domain.resumeanalysis.ResumeAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysisResponse {
    private String candidateId;
    private String candidateName;
    private ContactInfo contactInfo;
    private String candidateValueProposition;
    private ResumeAnalysis.TechnicalAnalysis technicalAnalysis;
    private ResumeAnalysis.ProfileAssessment profileAssessment;
    private ResumeAnalysis.InterviewerKeyInformation interviewerKeyInformation;
    private ResumeAnalysis.InterviewQuestions interviewQuestions;
    private List<ResumeAnalysis.Education> education;
    private List<ResumeAnalysis.RoleRecommendation> roleRecommendations;
    private ResumeAnalysis.AdditionalNotes additionalNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ResumeAnalysisResponse fromDto(
            ResumeAnalysisDto analysisDto,
            CandidateDto candidateDto
    ) {
        return ResumeAnalysisResponse.builder()
                .candidateId(analysisDto.getCandidateId())
                .candidateName(candidateDto.getCandidateName())
                .contactInfo(candidateDto.getContactInfo())
                .candidateValueProposition(analysisDto.getCandidateValueProposition())
                .technicalAnalysis(analysisDto.getTechnicalAnalysis())
                .profileAssessment(analysisDto.getProfileAssessment())
                .interviewerKeyInformation(analysisDto.getInterviewerKeyInformation())
                .interviewQuestions(analysisDto.getInterviewQuestions())
                .education(analysisDto.getEducation())
                .roleRecommendations(analysisDto.getRoleRecommendations())
                .additionalNotes(analysisDto.getAdditionalNotes())
                .createdAt(analysisDto.getCreatedAt())
                .updatedAt(analysisDto.getUpdatedAt())
                .build();
    }

}

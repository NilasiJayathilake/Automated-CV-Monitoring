package com.example.automatedcvmonitoring.application.resumeanalysis;

import com.example.automatedcvmonitoring.domain.candidates.ContactInfo;
import com.example.automatedcvmonitoring.domain.resumeanalysis.ResumeAnalysis;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResumeAnalysisDto {
    private String candidateId;
    private String candidateValueProposition;
    private List<ResumeAnalysis.RoleRecommendation> roleRecommendations;
    private ResumeAnalysis.TechnicalAnalysis technicalAnalysis;
    private ResumeAnalysis.ProfileAssessment profileAssessment;
    private ResumeAnalysis.InterviewerKeyInformation interviewerKeyInformation;
    private ResumeAnalysis.InterviewQuestions interviewQuestions;
    private List<ResumeAnalysis.Education> education;
    private ResumeAnalysis.AdditionalNotes additionalNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ResumeAnalysisDto fromEntity(ResumeAnalysis entity) {
        return ResumeAnalysisDto.builder()
                .candidateId(entity.getCandidateId())
                .candidateValueProposition(entity.getCandidateValueProposition())
                .roleRecommendations(entity.getRoleRecommendations() != null ? List.copyOf(entity.getRoleRecommendations()) : List.of())
                .technicalAnalysis(entity.getTechnicalAnalysis())
                .profileAssessment(entity.getProfileAssessment())
                .interviewerKeyInformation(entity.getInterviewerKeyInformation())
                .interviewQuestions(entity.getInterviewQuestions())
                .education(entity.getEducation() != null ? List.copyOf(entity.getEducation()) : List.of())
                .additionalNotes(entity.getAdditionalNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public ResumeAnalysis toEntity() {
        return ResumeAnalysis.builder()
                .candidateId(candidateId)
                .candidateValueProposition(candidateValueProposition)
                .roleRecommendations(roleRecommendations != null ? List.copyOf(roleRecommendations) : List.of())
                .technicalAnalysis(technicalAnalysis)
                .profileAssessment(profileAssessment)
                .interviewerKeyInformation(interviewerKeyInformation)
                .interviewQuestions(interviewQuestions)
                .education(education != null ? List.copyOf(education) : List.of())
                .additionalNotes(additionalNotes)
                .build();
    }
}

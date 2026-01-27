package com.example.automatedcvmonitoring.application.agents;

import com.example.automatedcvmonitoring.application.candidate.CandidateDto;
import com.example.automatedcvmonitoring.application.resumeanalysis.ResumeAnalysisDto;
import com.example.automatedcvmonitoring.domain.candidates.ContactInfo;
import com.example.automatedcvmonitoring.domain.resumeanalysis.ResumeAnalysis;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;


import java.util.List;

@Data
public class ResumeAnalysisCompletion {
        @JsonProperty(required = true)
        @JsonPropertyDescription("Candidate's full name. Return 'Not Provided' if absent.")
        private String candidateName;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Candidate's primary contact info including email, phone, and LinkedIn URL.")
        private ContactInfo contactInfo;

        @JsonProperty(required = true)
        @JsonPropertyDescription("A short 1-2 sentence value proposition summarizing the candidate’s strengths and career focus.")
        private String candidateValueProposition;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Detailed technical skills, including programming languages, frameworks, libraries, and tools/platforms.")
        private ResumeAnalysis.TechnicalAnalysis technicalAnalysis;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Recommended software engineering roles with justification, suitability score, and priority ranking.")
        private List<ResumeAnalysis.RoleRecommendation> roleRecommendations;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Profile assessment including key strengths and overall summary.")
        private ResumeAnalysis.ProfileAssessment profileAssessment;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Key information for interviewers including snapshot, projects to discuss, expertise areas, career trajectory, and potential concerns.")
        private ResumeAnalysis.InterviewerKeyInformation interviewerKeyInformation;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Interview questions categorized by type, with expected answers.")
        private ResumeAnalysis.InterviewQuestions interviewQuestions;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Education history of the candidate with institution, degree, duration, and relevant details.")
        private List<ResumeAnalysis.Education> education;

        @JsonPropertyDescription("Additional notes highlighting red flags or positive indicators from the resume.")
        private ResumeAnalysis.AdditionalNotes additionalNotes;

        public ResumeAnalysisDto toDto(String candidateId) {
            return ResumeAnalysisDto.builder()
                    .candidateId(candidateId)
                    .candidateValueProposition(candidateValueProposition)
                    .roleRecommendations(roleRecommendations)
                    .technicalAnalysis(technicalAnalysis)
                    .profileAssessment(profileAssessment)
                    .interviewerKeyInformation(interviewerKeyInformation)
                    .interviewQuestions(interviewQuestions)
                    .education(education)
                    .additionalNotes(additionalNotes)
                    .build();
        }

        public CandidateDto toCandidateDto(){
            return CandidateDto.builder()
                    .candidateName(candidateName)
                    .contactInfo(contactInfo)
                    .build();
        }


}

package com.example.automatedcvmonitoring.application.agents;

import com.example.automatedcvmonitoring.application.resumeanalysis.ResumeAnalysisDto;
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
        private ResumeAnalysisDto.ContactInfo contactInfo;

        @JsonProperty(required = true)
        @JsonPropertyDescription("A short 1-2 sentence value proposition summarizing the candidate’s strengths and career focus.")
        private String candidateValueProposition;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Detailed technical skills, including programming languages, frameworks, libraries, and tools/platforms.")
        private ResumeAnalysisDto.TechnicalAnalysis technicalAnalysis;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Recommended software engineering roles with justification, suitability score, and priority ranking.")
        private List<ResumeAnalysisDto.RoleRecommendation> roleRecommendations;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Profile assessment including key strengths and overall summary.")
        private ResumeAnalysisDto.ProfileAssessment profileAssessment;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Key information for interviewers including snapshot, projects to discuss, expertise areas, career trajectory, and potential concerns.")
        private ResumeAnalysisDto.InterviewerKeyInformation interviewerKeyInformation;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Interview questions categorized by type, with expected answers.")
        private ResumeAnalysisDto.InterviewQuestions interviewQuestions;

        @JsonProperty(required = true)
        @JsonPropertyDescription("Education history of the candidate with institution, degree, duration, and relevant details.")
        private List<ResumeAnalysisDto.Education> education;

        @JsonPropertyDescription("Additional notes highlighting red flags or positive indicators from the resume.")
        private ResumeAnalysisDto.AdditionalNotes additionalNotes;

        public ResumeAnalysisDto toDto() {
            return ResumeAnalysisDto.builder()
                    .candidateName(candidateName)
                    .contactInfo(contactInfo)
                    .candidateValueProposition(candidateValueProposition)
                    .technicalAnalysis(technicalAnalysis)
                    .roleRecommendations(roleRecommendations)
                    .profileAssessment(profileAssessment)
                    .interviewerKeyInformation(interviewerKeyInformation)
                    .interviewQuestions(interviewQuestions)
                    .education(education)
                    .additionalNotes(additionalNotes)
                    .build();
        }


}

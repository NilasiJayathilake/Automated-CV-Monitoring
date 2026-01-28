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

    @JsonProperty(value = "candidate_identification", required = true)
    @JsonPropertyDescription("Top-level candidate information including name, contact, and value proposition.")
    private CandidateIdentification candidateIdentification;

    @JsonProperty(value = "technical_analysis", required = true)
    @JsonPropertyDescription("Technical skill analysis extracted from the candidate's resume.")
    private TechnicalAnalysisCompletion technicalAnalysis;

    @JsonProperty(value = "profile_assessment", required = true)
    @JsonPropertyDescription("Profile assessment including key strengths and overall summary.")
    private ProfileAssessmentCompletion profileAssessment;

    @JsonProperty(value = "role_recommendations", required = true)
    @JsonPropertyDescription("Recommended roles for the candidate with justification, suitability score, and priority.")
    private RoleRecommendationsCompletion roleRecommendations;

    @JsonProperty(value = "interviewer_key_information", required = true)
    @JsonPropertyDescription("Key information for interviewers to know about the candidate.")
    private InterviewerKeyInformationCompletion interviewerKeyInformation;

    @JsonProperty(value = "interview_questions", required = true)
    @JsonPropertyDescription("Suggested interview questions categorized by type with expected answers.")
    private ResumeAnalysis.InterviewQuestions interviewQuestions;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Educational background of the candidate.")
    private List<ResumeAnalysis.Education> education;

    @JsonProperty(value = "additional_notes", required = true)
    @JsonPropertyDescription("Additional notes highlighting red flags, positive indicators, and diverse background experience.")
    private AdditionalNotesCompletion additionalNotes;


    @Data
    public static class CandidateIdentification {

        @JsonProperty(value = "candidate_name", required = true)
        @JsonPropertyDescription("Extract the candidate's full name. Return 'Not Provided' if absent.")
        private String candidateName;

        @JsonProperty(value = "contact_info", required = true)
        @JsonPropertyDescription("Candidate's primary contact info including email, phone, and LinkedIn URL.")
        private ContactInfo contactInfo;

        @JsonProperty(value = "candidate_value_proposition", required = true)
        @JsonPropertyDescription("Craft a 1-2 sentence pitch summarizing the candidate’s unique strengths and career focus.")
        private String candidateValueProposition;
    }

    @Data
    public static class TechnicalAnalysisCompletion {

        @JsonProperty(value = "skills", required = true)
        @JsonPropertyDescription("Detailed technical skills categorized by type.")
        private Skills skills;

        @JsonProperty(required = true)
        @JsonPropertyDescription("List any relevant professional certifications. Leave empty if none.")
        private List<String> certifications;

        @Data
        public static class Skills {

            @JsonProperty(value = "programming_languages", required = true)
            @JsonPropertyDescription("List specific languages mentioned, e.g., Python, Java, C++.")
            private List<String> programmingLanguages;

            @JsonProperty(value = "frameworks_libraries", required = true)
            @JsonPropertyDescription("List mentioned frameworks and libraries, e.g., React, Django, TensorFlow.")
            private List<String> frameworksLibraries;

            @JsonProperty(value = "tools_platforms", required = true)
            @JsonPropertyDescription("List mentioned tools, platforms, and software, e.g., Docker, AWS, Git, Jira.")
            private List<String> toolsPlatforms;
        }

        public ResumeAnalysis.TechnicalAnalysis toEntity() {
            return ResumeAnalysis.TechnicalAnalysis.builder()
                    .programmingLanguages(skills.getProgrammingLanguages())
                    .frameworksLibraries(skills.getFrameworksLibraries())
                    .toolsPlatforms(skills.getToolsPlatforms())
                    .certifications(certifications)
                    .build();
        }
    }

    @Data
    public static class ProfileAssessmentCompletion {

        @JsonProperty(value = "key_strengths", required = true)
        @JsonPropertyDescription("List key strengths of the candidate from resume and projects.")
        private List<String> keyStrengths;

        @JsonProperty(value = "profile_summary", required = true)
        @JsonPropertyDescription("A short summary of the candidate's overall profile.")
        private String profileSummary;

        public ResumeAnalysis.ProfileAssessment toEntity() {
            return ResumeAnalysis.ProfileAssessment.builder()
                    .keyStrengths(keyStrengths)
                    .profileSummary(profileSummary)
                    .build();
        }
    }

    @Data
    public static class RoleRecommendationsCompletion {

        @JsonProperty(value = "recommended_roles", required = true)
        @JsonPropertyDescription("Recommended roles including job title, suitability score, justification, and priority rank.")
        private List<ResumeAnalysis.RoleRecommendation> recommendedRoles;
    }

    @Data
    public static class InterviewerKeyInformationCompletion {

        @JsonProperty(value = "quick_snapshot", required = true)
        @JsonPropertyDescription("A 1-2 sentence summary describing the candidate as an intern or entry-level profile.")
        private String quickSnapshot;

        @JsonProperty(value = "talk_about_topics", required = true)
        @JsonPropertyDescription("3-5 academic projects, personal projects, tools, or technologies that would be good interview discussion points.")
        private List<String> talkAboutTopics;

        @JsonProperty(value = "expertise_areas", required = true)
        @JsonPropertyDescription("Technical areas including strongest exposure and skills currently being learned.")
        private ExpertiseAreasCompletion expertiseAreas;

        @JsonProperty(value = "growth_indicators", required = true)
        @JsonPropertyDescription("Signs of learning mindset, self-initiative, or rapid skill acquisition.")
        private List<String> growthIndicators;

        @JsonProperty(value = "areas_to_probe", required = true)
        @JsonPropertyDescription("Aspects that require clarification during interview.")
        private List<String> areasToProbe;

        public ResumeAnalysis.InterviewerKeyInformation toEntity() {
            return ResumeAnalysis.InterviewerKeyInformation.builder()
                    .quickSnapshot(quickSnapshot)
                    .talkAboutTopics(talkAboutTopics)
                    .expertiseAreas(expertiseAreas.toEntity())
                    .growthIndicators(growthIndicators)
                    .potentialConcerns(areasToProbe)
                    .build();
        }
    }

    @Data
    public static class ExpertiseAreasCompletion {

        @JsonProperty(value = "strongest_exposure", required = true)
        @JsonPropertyDescription("Technical area where the candidate has the most hands-on exposure or project work.")
        private String strongestExposure;

        @JsonProperty(value = "learning_in_progress", required = true)
        @JsonPropertyDescription("Skills, tools, or technologies the candidate is currently learning or has recently started using.")
        private List<String> learningInProgress;

        public ResumeAnalysis.InterviewerKeyInformation.ExpertiseAreas toEntity() {
            return ResumeAnalysis.InterviewerKeyInformation.ExpertiseAreas.builder()
                    .strongestArea(strongestExposure)
                    .emergingSkills(learningInProgress)
                    .build();
        }
    }

    @Data
    public static class AdditionalNotesCompletion {

        @JsonProperty(value = "red_flags", required = true)
        @JsonPropertyDescription("Note any items that may need clarification during the interview. State 'None apparent' if no issues are found.")
        private String redFlags;

        @JsonProperty(value = "positive_indicators", required = true)
        @JsonPropertyDescription("Self-initiated projects, hackathons, open-source contributions, and transferable skills from work or education.")
        private String positiveIndicators;

        @JsonProperty(value = "diverse_background", required = true)
        @JsonPropertyDescription("Experience in other professions or domains showing adaptability, soft skills, or initiative.")
        private List<String> diverseBackground;

        public ResumeAnalysis.AdditionalNotes toEntity() {
            return ResumeAnalysis.AdditionalNotes.builder()
                    .redFlags(redFlags)
                    .positiveIndicators(positiveIndicators)
                    .build();
        }
    }


    public ResumeAnalysisDto toDto(String candidateId) {
        return ResumeAnalysisDto.builder()
                .candidateId(candidateId)
                .candidateValueProposition(candidateIdentification.getCandidateValueProposition())
                .technicalAnalysis(technicalAnalysis.toEntity())
                .profileAssessment(profileAssessment.toEntity())
                .interviewerKeyInformation(interviewerKeyInformation.toEntity())
                .roleRecommendations(roleRecommendations.getRecommendedRoles())
                .interviewQuestions(interviewQuestions)
                .education(education)
                .additionalNotes(additionalNotes.toEntity())
                .build();
    }

    public CandidateDto toCandidateDto() {
        return CandidateDto.builder()
                .candidateName(candidateIdentification.getCandidateName())
                .contactInfo(candidateIdentification.getContactInfo())
                .build();
    }
}


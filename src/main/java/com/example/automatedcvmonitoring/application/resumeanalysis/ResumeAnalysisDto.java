package com.example.automatedcvmonitoring.application.resumeanalysis;

import com.example.automatedcvmonitoring.domain.resumeanalysis.ContactInfo;
import com.example.automatedcvmonitoring.domain.resumeanalysis.ProfileAssessment;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ResumeAnalysisDto {

    private String candidateName;
    private ContactInfo contactInfo;
    private String candidateValueProposition;
    private TechnicalAnalysis technicalAnalysis;
    private ProfileAssessment profileAssessment;



    @Data
    @Builder
    public static class TechnicalAnalysis {
        private List<String> programmingLanguages;
        private List<String> frameworksLibraries;
        private List<String> toolsPlatforms;
        private List<String> certifications;
    }

    @Data
    @Builder
    public static class RoleRecommendation {
        private String jobTitle;
        private Integer suitabilityScore;
        private String justification;
        private Integer priorityRank;
    }



    @Data
    @Builder
    public static class InterviewerKeyInformation {
        private String quickSnapshot;
        private List<String> talkAboutTopics;
        private ExpertiseAreas expertiseAreas;
        private String careerTrajectory;
        private List<String> potentialConcerns;
        private List<String> redFlagsToProbe;

        @Data
        @Builder
        public static class ExpertiseAreas {
            private String strongestArea;
            private List<String> emergingSkills;
        }
    }

    @Data
    @Builder
    public static class InterviewQuestions {
        private List<QuestionAnswer> technicalDeepDive;
        private List<QuestionAnswer> projectSpecific;
        private List<QuestionAnswer> behavioralSituational;
        private List<QuestionAnswer> roleFitQuestions;
        private List<QuestionAnswer> growthMindset;

        @Data
        @Builder
        public static class QuestionAnswer {
            private String question;
            private String expectedAnswer;
        }
    }

    @Data
    @Builder
    public static class Education {
        private String institution;
        private String degree;
        private String duration;
        private List<String> details;
    }

    @Data
    @Builder
    public static class AdditionalNotes {
        private String redFlags;
        private String positiveIndicators;
    }
}

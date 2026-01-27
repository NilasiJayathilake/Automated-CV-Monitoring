package com.example.automatedcvmonitoring.domain.resumeanalysis;

import com.example.automatedcvmonitoring.application.resumeanalysis.ResumeAnalysisDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "resume_analysis")
public class ResumeAnalysis {
    @Id
    private String id;
    private String candidateName;
    private ContactInfo contactInfo;
    private String candidateValueProposition;
    private ProfileAssessment profileAssessment;
}

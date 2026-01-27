package com.example.automatedcvmonitoring.domain.candidates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "candidates")
public class Candidate {
    @Id
    private String id;
    private String candidateName;
    private ContactInfo contactInfo;
    private String hrAssessment;
    private String primaryAssessment;
    private String finalAssessment;
}

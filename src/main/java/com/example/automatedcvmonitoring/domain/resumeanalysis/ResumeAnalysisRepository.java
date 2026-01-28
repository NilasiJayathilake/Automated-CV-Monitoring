package com.example.automatedcvmonitoring.domain.resumeanalysis;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeAnalysisRepository extends MongoRepository<ResumeAnalysis, String> {
    Optional<ResumeAnalysis> findByCandidateId(String candidateId);
}

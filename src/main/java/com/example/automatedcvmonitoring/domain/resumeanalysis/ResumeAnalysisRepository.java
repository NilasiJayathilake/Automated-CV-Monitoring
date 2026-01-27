package com.example.automatedcvmonitoring.domain.resumeanalysis;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResumeAnalysisRepository extends MongoRepository<ResumeAnalysis, String> {
}

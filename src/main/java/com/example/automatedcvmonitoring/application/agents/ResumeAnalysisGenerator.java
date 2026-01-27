package com.example.automatedcvmonitoring.application.agents;

import com.example.automatedcvmonitoring.application.resumeanalysis.ResumeAnalysisDto;
import com.example.automatedcvmonitoring.infrastructure.file.MarkdownFileReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeAnalysisGenerator {
    private static final String SYSTEM_PROMPT = MarkdownFileReader
            .readFileContent("prompts/cv-analyzer-prompt.md");
    private final ChatClient chatClient;
    private final ResumeContextBuilder resumeContextBuilder;

    public ResumeAnalysisDto buildResumeAnalysis() throws IOException {
        String resumeText = resumeContextBuilder.buildLatestResumeContext();
        long startTime = System.currentTimeMillis();
        log.info("[AI-AGENT-START] ResumeAnalysisGenerator - buildResumeAnalysis started");

        String userPrompt = String.format("%s", resumeText);

        ResumeAnalysisDto analysisDto = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userPrompt)
                .call()
                .entity(ResumeAnalysisDto.class);

        long duration = System.currentTimeMillis() - startTime;
        log.info("[AI-AGENT-END] ResumeAnalysisGenerator - buildResumeAnalysis completed in {}ms", duration);

        return analysisDto;
    }

}

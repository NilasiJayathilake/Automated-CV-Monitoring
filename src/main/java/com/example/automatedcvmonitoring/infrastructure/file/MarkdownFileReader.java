package com.example.automatedcvmonitoring.infrastructure.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class MarkdownFileReader {
    public static String readFileContent(String classpathLocation) {
        ClassPathResource resource = new ClassPathResource(classpathLocation);
        log.info("Reading prompt file from classpath: {}", resource.getPath());
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Failed to read prompt file from classpath: {}", classpathLocation, e);
            throw new IllegalStateException("Could not read prompt file", e);
        }
    }
}


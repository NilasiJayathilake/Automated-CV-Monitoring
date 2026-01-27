package com.example.automatedcvmonitoring.application.agents;

import com.example.automatedcvmonitoring.infrastructure.resume.ResumeStoreConfig;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ResumeContextBuilder {

    private final ResumeStoreConfig config;

    // Reads all PDFs from the configured local folder and returns their text.
    public Map<String, String> buildAllResumeContexts() throws IOException {
        Path baseDir = Paths.get(config.getBaseDirectory());
        if (!Files.exists(baseDir) || !Files.isDirectory(baseDir)) {
            return Collections.emptyMap();
        }

        try (var stream = Files.list(baseDir)) {
            List<Path> pdfs = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".pdf"))
                    .toList();

            Map<String, String> results = new LinkedHashMap<>();
            for (Path pdf : pdfs) {
                String text = extractTextFromPdf(pdf);
                results.put(pdf.getFileName().toString(), text);
            }
            return results;
        }
    }

    // Reads the Latest Resume
    public String buildLatestResumeContext() throws IOException {
        Path baseDir = Paths.get(config.getBaseDirectory());
        if (!Files.exists(baseDir) || !Files.isDirectory(baseDir)) {
            return "No resume found";
        }

        try (var stream = Files.list(baseDir)) {
            Optional<Path> newestPdf = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".pdf"))
                    .max(Comparator.comparing(p -> {
                        try {
                            FileTime t = Files.getLastModifiedTime(p);
                            return t.toMillis();
                        } catch (IOException e) {
                            return 0L;
                        }
                    }));

            if (newestPdf.isEmpty()) {
                return "No resume found";
            }
            return extractTextFromPdf(newestPdf.get());
        }
    }


    private String extractTextFromPdf(Path pdfPath) throws IOException {
        try (PDDocument doc = PDDocument.load(pdfPath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(doc);
        }
    }
}
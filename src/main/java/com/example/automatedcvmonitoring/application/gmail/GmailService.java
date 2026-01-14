package com.example.automatedcvmonitoring.application.gmail;

import com.example.automatedcvmonitoring.api.configuration.GoogleOAuthConfig;
import com.example.automatedcvmonitoring.api.v1.gmail.GoogleCredentialsDto;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GmailService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Automated-CV-Monitoring";
    private static final String TOPIC = "projects/careerday-484108/topics/limark_cv_monitor";

    private final GoogleOAuthConfig config;

    /**
     * Creates the authorization URL for Google OAuth flow
     */
    public String getAuthorizationUrl(String state) throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = loadClientSecrets();

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                config.getScopes()
        )
                .setAccessType("offline")
                .build();
        log.info("Redirect URI being used: {}", config.getRedirectUri());

        return flow.newAuthorizationUrl()
                .setRedirectUri(config.getRedirectUri())
                .setState(state)
                .set("prompt", "consent")
                .set("include_granted_scopes", "true")
                .build();
    }

    /**
     * Exchanges authorization code for credentials
     */
    public GoogleCredentialsDto exchangeCode(String code) throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleClientSecrets clientSecrets = loadClientSecrets();

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                config.getScopes()
        )
                .setAccessType("offline")
                .build();

        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                .setRedirectUri(config.getRedirectUri())
                .execute();

        return GoogleCredentialsDto.builder()
                .token(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .tokenUri("https://oauth2.googleapis.com/token")
                .clientId(clientSecrets.getDetails().getClientId())
                .clientSecret(clientSecrets.getDetails().getClientSecret())
                .scopes(config.getScopes())
                .build();
    }

    /**
     * Polls recent emails and downloads PDF attachments
     */
    public List<GmailMessageDto> pollEmails(GoogleCredentialsDto credentialsDto, int maxResults) throws Exception {
        Gmail service = buildGmailService(credentialsDto);

        List<Message> messages = service.users().messages()
                .list("me")
                .setMaxResults((long) maxResults)
                .execute()
                .getMessages();

        if (messages == null || messages.isEmpty()) {
            log.info("No messages found.");
            return Collections.emptyList();
        }

        List<GmailMessageDto> emailMessages = new ArrayList<>();
        Set<String> downloadedAttachments = new HashSet<>();

        for (Message message : messages) {
            Message fullMessage = service.users().messages()
                    .get("me", message.getId())
                    .setFormat("full")
                    .execute();

            GmailMessageDto emailDto = processMessage(service, fullMessage, downloadedAttachments);
            emailMessages.add(emailDto);
        }

        return emailMessages;
    }

    /**
     * Process a single email message
     */
    private GmailMessageDto processMessage(Gmail service, Message message, Set<String> downloadedAttachments) {
        String subject = getHeader(message, "Subject");
        if (subject == null) {
            subject = "(no subject)";
        }

        StringBuilder bodyBuilder = new StringBuilder();
        List<String> attachments = new ArrayList<>();

        MessagePart payload = message.getPayload();
        if (payload != null && payload.getParts() != null) {
            processMessageParts(service, message.getId(), payload.getParts(), bodyBuilder, attachments, downloadedAttachments);
        }

        return GmailMessageDto.builder()
                .subject(subject)
                .body(bodyBuilder.toString())
                .attachments(attachments)
                .build();
    }

    /**
     * Recursively process message parts (body and attachments)
     */
    private void processMessageParts(Gmail service, String messageId, List<MessagePart> parts,
                                      StringBuilder bodyBuilder, List<String> attachments,
                                      Set<String> downloadedAttachments) {
        for (MessagePart part : parts) {
            String mimeType = part.getMimeType();
            String filename = part.getFilename();

            // Recursively process nested parts
            if (part.getParts() != null && !part.getParts().isEmpty()) {
                processMessageParts(service, messageId, part.getParts(), bodyBuilder, attachments, downloadedAttachments);
            }

            // Extract plain text body
            if ("text/plain".equals(mimeType) && (filename == null || filename.isEmpty())) {
                MessagePartBody body = part.getBody();
                if (body != null && body.getData() != null) {
                    String decodedBody = new String(Base64.getUrlDecoder().decode(body.getData()));
                    bodyBuilder.append(decodedBody);
                }
            }

            // Handle PDF attachments
            if (filename != null && !filename.isEmpty() && filename.toLowerCase().endsWith(".pdf")) {
                String savedFilename = savePdfAttachment(service, messageId, part, downloadedAttachments);
                if (savedFilename != null) {
                    attachments.add(savedFilename);
                }
            }
        }
    }

    /**
     * Save PDF attachment to disk
     */
    private String savePdfAttachment(Gmail service, String messageId, MessagePart part,
                                     Set<String> downloadedAttachments) {
        String filename = part.getFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            return null;
        }

        // Sanitize filename
        filename = filename.replace("/", "_")
                .replace("\\", "_")
                .replaceAll("\\.$", "")
                .trim();

        MessagePartBody attachmentBody = part.getBody();
        String attachmentId = attachmentBody.getAttachmentId();

        if (attachmentId == null || downloadedAttachments.contains(attachmentId)) {
            return null;
        }

        try {
            MessagePartBody attachmentData = service.users().messages().attachments()
                    .get("me", messageId, attachmentId)
                    .execute();

            byte[] fileData = Base64.getUrlDecoder().decode(attachmentData.getData());
            downloadedAttachments.add(attachmentId);

            // Create downloads directory if not exists
            Path downloadsPath = Paths.get(config.getDownloadsDir());
            Files.createDirectories(downloadsPath);

            Path filePath = downloadsPath.resolve(filename);
            Files.write(filePath, fileData);

            log.info("📄 Saved PDF: {} ({} bytes)", filePath, fileData.length);
            return filename;

        } catch (Exception e) {
            log.error("Error saving attachment: {}", filename, e);
            return null;
        }
    }

    /**
     * Build Gmail service from credentials
     */
    private Gmail buildGmailService(GoogleCredentialsDto credentialsDto) throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleCredentials credentials = GoogleCredentials.create(
                new AccessToken(credentialsDto.getToken(), null)
        );

        return new Gmail.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Load client secrets from resources
     */
    private GoogleClientSecrets loadClientSecrets() throws IOException {
        String secretsFile = config.getClientSecretsFile();

        // Remove "classpath:" prefix if present
        if (secretsFile.startsWith("classpath:")) {
            secretsFile = secretsFile.substring("classpath:".length());
        }

        ClassPathResource resource = new ClassPathResource(secretsFile);
        try (InputStream in = resource.getInputStream();
             InputStreamReader reader = new InputStreamReader(in)) {
            return GoogleClientSecrets.load(JSON_FACTORY, reader);
        }
    }

    /**
     * Extract header value from message
     */
    private String getHeader(Message message, String headerName) {
        if (message.getPayload() == null || message.getPayload().getHeaders() == null) {
            return null;
        }

        return message.getPayload().getHeaders().stream()
                .filter(header -> headerName.equalsIgnoreCase(header.getName()))
                .map(com.google.api.services.gmail.model.MessagePartHeader::getValue)
                .findFirst()
                .orElse(null);
    }
}



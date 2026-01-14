package com.example.automatedcvmonitoring.api.v1.gmail;

import com.example.automatedcvmonitoring.api.configuration.GoogleOAuthConfig;
import com.example.automatedcvmonitoring.application.gmail.GmailMessageDto;
import com.example.automatedcvmonitoring.application.gmail.GmailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/gmail")
@RequiredArgsConstructor
public class GmailController {

    private static final String CREDENTIALS_SESSION_KEY = "google_credentials";
    private static final String STATE_SESSION_KEY = "oauth_state";

    private final GmailService gmailService;
    private final GoogleOAuthConfig config;

    @GetMapping("/auth")
    public ResponseEntity<?> initiateGoogleAuth(HttpSession session) {
        log.info("Initiating Google Auth");
        try {
            String state = UUID.randomUUID().toString();
            session.setAttribute(STATE_SESSION_KEY, state);

            String authorizationUrl = gmailService.getAuthorizationUrl(state);

            log.info("🔐 Redirecting to Google OAuth {}", authorizationUrl);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(authorizationUrl))
                    .build();

        } catch (Exception e) {
            log.error("Error initiating OAuth", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to initiate Google authentication: " + e.getMessage());
        }
    }

    /**
     * OAuth callback endpoint
     * GET /api/v1/gmail/auth/callback?code=...&state=...
     */
    @GetMapping("/auth/callback")
    public ResponseEntity<String> handleOAuthCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpSession session) {

        try {
            String sessionState = (String) session.getAttribute(STATE_SESSION_KEY);

            if (sessionState == null) {
                return ResponseEntity.badRequest()
                        .body("Session expired. Please restart the login process.");
            }

            if (!sessionState.equals(state)) {
                return ResponseEntity.badRequest()
                        .body("Invalid state parameter. Possible CSRF attack.");
            }

            // Exchange code for credentials
            GoogleCredentialsDto credentials = gmailService.exchangeCode(code);
            session.setAttribute(CREDENTIALS_SESSION_KEY, credentials);

            log.info("✅ OAuth success - redirecting to: {}", config.getSuccessRedirectUri());

            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(config.getSuccessRedirectUri()))
                    .build();

        } catch (Exception e) {
            log.error("Error handling OAuth callback", e);
            return ResponseEntity.internalServerError()
                    .body("Authentication failed: " + e.getMessage());
        }
    }

    /**
     * Poll recent emails and download PDF attachments
     * GET /api/v1/gmail/poll?maxResults=5
     */
    @GetMapping("/poll")
    public ResponseEntity<?> pollEmails(
            @RequestParam(defaultValue = "5") int maxResults,
            HttpSession session) {

        try {
            GoogleCredentialsDto credentials = (GoogleCredentialsDto) session.getAttribute(CREDENTIALS_SESSION_KEY);

            if (credentials == null) {
                return ResponseEntity.status(401)
                        .body("No credentials found. Please login first at /api/v1/gmail/auth/google");
            }

            List<GmailMessageDto> emails = gmailService.pollEmails(credentials, maxResults);

            if (emails.isEmpty()) {
                return ResponseEntity.ok("No emails found.");
            }

            // Build HTML response similar to Python version
            StringBuilder html = new StringBuilder("<html><body>");
            for (GmailMessageDto email : emails) {
                html.append("<strong>Subject:</strong> ").append(email.getSubject()).append("<br>");
                html.append("<strong>Body:</strong><br>")
                        .append(email.getBody().replace("\n", "<br>"))
                        .append("<br>");

                if (!email.getAttachments().isEmpty()) {
                    html.append("<strong>Attachments:</strong> ")
                            .append(String.join(", ", email.getAttachments()));
                }

                html.append("<hr>");
            }
            html.append("</body></html>");

            return ResponseEntity.ok(html.toString());

        } catch (Exception e) {
            log.error("Error polling emails", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to poll emails: " + e.getMessage());
        }
    }

    @GetMapping("/poll/json")
    public ResponseEntity<?> pollEmailsJson(
            @RequestParam(defaultValue = "5") int maxResults,
            HttpSession session) {

        try {
            GoogleCredentialsDto credentials = (GoogleCredentialsDto) session.getAttribute(CREDENTIALS_SESSION_KEY);

            if (credentials == null) {
                return ResponseEntity.status(401)
                        .body("No credentials found. Please login first at /api/v1/gmail/auth/google");
            }

            List<GmailMessageDto> emails = gmailService.pollEmails(credentials, maxResults);
            return ResponseEntity.ok(emails);

        } catch (Exception e) {
            log.error("Error polling emails", e);
            return ResponseEntity.internalServerError()
                    .body("Failed to poll emails: " + e.getMessage());
        }
    }

    /**
     * Webhook endpoint for Gmail push notifications (optional)
     * POST /api/v1/gmail/webhooks
     */
    @PostMapping("/webhooks")
    public ResponseEntity<String> handleGmailWebhook(@RequestBody(required = false) String payload) {
        try {
            log.info("📩 Gmail notification received: {}", payload);
            // Process webhook notification here if needed
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            log.error("⚠️ Webhook error", e);
            return ResponseEntity.ok("ok");
        }
    }

    /**
     * Check authentication status
     * GET /api/v1/gmail/auth/status
     */
    @GetMapping("/auth/status")
    public ResponseEntity<?> checkAuthStatus(HttpSession session) {
        GoogleCredentialsDto credentials = (GoogleCredentialsDto) session.getAttribute(CREDENTIALS_SESSION_KEY);
        boolean isAuthenticated = credentials != null;

        return ResponseEntity.ok(new AuthStatusResponse(isAuthenticated));
    }

    // Response DTOs
    record AuthUrlResponse(String authorizationUrl) {}
    record AuthStatusResponse(boolean authenticated) {}
}


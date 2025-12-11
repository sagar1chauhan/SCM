package com.scm.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SendinblueApiService {

    @Value("${sendinblue.api.key}")
    private String apiKey;

    private final RestTemplate rest = new RestTemplate();

    public void sendTransactionalEmail(String toEmail, String subject, String htmlContent) {
        String url = "https://api.sendinblue.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> payload = Map.of(
            "sender", Map.of("name", "Your App", "email", "no-reply@yourdomain.com"),
            "to", new Object[] { Map.of("email", toEmail) },
            "subject", subject,
            "htmlContent", htmlContent
        );

        HttpEntity<Map<String,Object>> request = new HttpEntity<>(payload, headers);
        rest.postForEntity(url, request, String.class);
    }
}

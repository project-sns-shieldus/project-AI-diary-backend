package com.restapi.project_AI_diary_backend.domain.generator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/keys")
public class ApiKeyController {

    @Value("${GOOGLE_TRANSLATE_API_KEY}")
    private String googleTranslateApiKey;

    @Value("${DALLE_API_KEY}")
    private String dalleApiKey;

    // Google Translate API 키 전달
    @GetMapping("/google-translate")
    public ResponseEntity<Map<String, String>> getGoogleTranslateApiKey() {
        Map<String, String> response = new HashMap<>();
        response.put("apiKey", googleTranslateApiKey);
        return ResponseEntity.ok(response);
    }

    // DALL·E API 키 전달
    @GetMapping("/dalle")
    public ResponseEntity<Map<String, String>> getDalleApiKey() {
        Map<String, String> response = new HashMap<>();
        response.put("apiKey", dalleApiKey);
        return ResponseEntity.ok(response);
    }
}

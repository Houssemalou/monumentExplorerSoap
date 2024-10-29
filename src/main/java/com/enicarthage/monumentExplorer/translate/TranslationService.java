package com.enicarthage.monumentExplorer.translate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final RestTemplate restTemplate;

    @Value("${libretranslate.url}")
    private String libreTranslateUrl;

    public String translate(String text, String targetLang, String sourceLang) {

        if (sourceLang == null || sourceLang.isEmpty()) {
            sourceLang = "en";
        }

        String url = libreTranslateUrl + "/translate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("q", text);
        requestBody.put("source", sourceLang);
        requestBody.put("target", targetLang);
        requestBody.put("format", "text");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        Map<String, String> responseBody = response.getBody();
        return responseBody != null ? responseBody.get("translatedText") : null;
    }
}


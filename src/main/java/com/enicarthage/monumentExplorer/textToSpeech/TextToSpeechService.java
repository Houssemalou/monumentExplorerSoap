package com.enicarthage.monumentExplorer.textToSpeech;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TextToSpeechService {

    @Value("${azure.openai.tts.url}")
    private String apiUrl;

    @Value("${azure.openai.api.key}")
    private String apiKey;

    public String convertTextToAudio(String text) throws IOException {
        String outputFilePath = generateOutputFilePath(text);
        RestTemplate restTemplate = new RestTemplate();
        String requestBody = "{"
                + "\"model\": \"tts-1-hd\","
                + "\"input\": \"" + text + "\","
                + "\"voice\": \"" + "alloy" + "\""
                + "}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                byte[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(response.getBody());
            }
            System.out.println("Fichier audio enregistré avec succès : " + outputFilePath);
            return outputFilePath;
        } else {
            throw new RuntimeException("Erreur lors de la conversion du texte en audio : " + response.getStatusCode());
        }
    }

    private String generateOutputFilePath(String text) {

        String directory = "audio_files";
        Path dirPath = Paths.get(directory);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "audio_" + timestamp + ".mp3";
        return dirPath.resolve(fileName).toString();
    }
}




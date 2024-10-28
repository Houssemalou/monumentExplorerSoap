package com.enicarthage.monumentExplorer.imageRecognition;

import com.enicarthage.monumentExplorer.monument.Monument;
import com.enicarthage.monumentExplorer.monument.MonumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/monuments")
@RequiredArgsConstructor
public class MonumentRecognitionController {
    private final ImageRecognitionService imageRecognitionService;
    private final MonumentRepository monumentRepository;

    @PostMapping("/recognize")
    public ResponseEntity<Monument> recognizeMonument(@RequestParam("image") MultipartFile file) {
        try {
            String monumentName = imageRecognitionService.recognizeMonument(file.getBytes());

            if (monumentName != null) {
                Monument monument = monumentRepository.findByNameContaining(monumentName).stream().findFirst().orElse(null);
                if (monument != null) {
                    return ResponseEntity.ok(monument);  // Return the recognized monument's details
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Monument not found in the database

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

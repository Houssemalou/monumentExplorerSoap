package com.enicarthage.monumentExplorer.textToSpeech;

import com.enicarthage.monumentExplorer.monument.Monument;
import com.enicarthage.monumentExplorer.monument.MonumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/monuments")
@RequiredArgsConstructor
public class TextToSpeechController {


    private final TextToSpeechService textToSpeechService;


    private final MonumentRepository monumentRepository;

    @GetMapping("/{id}/audio")
    public ResponseEntity<byte[]> getMonumentAudioDescription(@PathVariable Integer id) {
        Monument monument = monumentRepository.findById(id).orElse(null);

        if (monument == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            byte[] audio = textToSpeechService.generateSpeech(monument.getHistoricalDetails());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(audio);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


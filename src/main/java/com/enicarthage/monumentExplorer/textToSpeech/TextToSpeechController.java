package com.enicarthage.monumentExplorer.textToSpeech;

import com.enicarthage.monumentExplorer.monument.Monument;
import com.enicarthage.monumentExplorer.monument.MonumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/monuments")
@RequiredArgsConstructor
public class TextToSpeechController {


    private final TextToSpeechService textToSpeechService;


    private final MonumentRepository monumentRepository;

    @GetMapping("/{id}/convert")
    public String convertTextToAudio(@PathVariable Integer id) {
        Monument monument = monumentRepository.findById(id).orElse(null);
        if (monument == null) {
            return "monument not found";
        }
        try {
            return textToSpeechService.convertTextToAudio(monument.getHistoricalDetails());
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la création du fichier audio.";
        }
    }

}


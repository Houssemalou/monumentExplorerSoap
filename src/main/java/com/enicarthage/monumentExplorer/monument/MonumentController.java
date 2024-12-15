package com.enicarthage.monumentExplorer.monument;

import com.enicarthage.monumentExplorer.translate.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/monument")
@RequiredArgsConstructor
public class MonumentController {

    private final MonumentRepository monumentRepository;
    private final TranslationService translationService;

    @GetMapping("/filter")
    public List<Monument> getMonuments(@RequestParam(required = false) String country,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String targetLang) {
        List<Monument> monuments;

        if (country != null) {
            monuments = monumentRepository.findByCountry(country);
        } else if (name != null) {
            monuments = monumentRepository.findByNameContaining(name);
        } else {
            monuments = monumentRepository.findAll();
        }

        if (targetLang != null) {
            return monuments.stream()
                    .map(monument -> translateMonumentFields(monument, targetLang))
                    .collect(Collectors.toList());
        }

        return monuments;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monument> getMonumentById(@PathVariable Integer id,
                                                    @RequestParam(required = false) String targetLang) {
        return monumentRepository.findById(id)
                .map(monument -> {
                    if (targetLang != null) {
                        monument = translateMonumentFields(monument, targetLang);
                    }
                    return ResponseEntity.ok().body(monument);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private Monument translateMonumentFields(Monument monument, String targetLang) {
        monument.setName(translationService.translate(monument.getName(), targetLang, "en"));
        monument.setCountry(translationService.translate(monument.getCountry(), targetLang, "en"));
        monument.setHistoricalDetails(translationService.translate(monument.getHistoricalDetails(), targetLang, "en"));
        return monument;
    }
}

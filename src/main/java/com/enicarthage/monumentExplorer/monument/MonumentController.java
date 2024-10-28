package com.enicarthage.monumentExplorer.monument;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monument")
@RequiredArgsConstructor
public class MonumentController {

    private final MonumentRepository monumentRepository;

    @GetMapping("/filter")
    public List<Monument> getMonuments(@RequestParam(required = false) String country,
                                       @RequestParam(required = false) String name) {
        if (country != null) {
            return monumentRepository.findByCountry(country);
        } else if (name != null) {
            return monumentRepository.findByNameContaining(name);
        } else {
            return monumentRepository.findAll();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monument> getMonumentById(@PathVariable Integer id) {
        return monumentRepository.findById(id)
                .map(monument -> ResponseEntity.ok().body(monument))
                .orElse(ResponseEntity.notFound().build());
    }
}

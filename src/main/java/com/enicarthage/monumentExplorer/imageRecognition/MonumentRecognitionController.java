package com.enicarthage.monumentExplorer.imageRecognition;

import com.enicarthage.monumentExplorer.monument.Monument;
import com.enicarthage.monumentExplorer.monument.MonumentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping("/api/monuments")
@RequiredArgsConstructor
public class MonumentRecognitionController {
    private final ImageRecognitionService imageRecognitionService;


    @GetMapping("/{imageUrl}")
    public ResponseEntity<Monument> getMonument(@PathVariable("imageUrl") String imageUrl) {
        Monument closestMonument = imageRecognitionService.findClosestMonument();
        if (closestMonument != null) {
            return ResponseEntity.ok(closestMonument);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}

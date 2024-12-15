package com.enicarthage.monumentExplorer.imageRecognition;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClient;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClientBuilder;
import com.azure.ai.vision.imageanalysis.models.*;
import com.azure.core.credential.KeyCredential;
import com.enicarthage.monumentExplorer.monument.Monument;
import com.enicarthage.monumentExplorer.monument.MonumentRepository;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageRecognitionService {

    private final MonumentRepository monumentRepository;

    @Value("${azure.openai.endpoint}")
    private String endpoint;

    @Value("${azure.openai.api.key1}")
    private String apiKey;

    public String recognizeMonument() {

        ImageAnalysisClient client = new ImageAnalysisClientBuilder()
                .endpoint(endpoint)
                .credential(new KeyCredential(apiKey))
                .buildClient();


        ImageAnalysisResult result = client.analyzeFromUrl(
                "https://www.merveilles-du-monde.com/Etudes/images/Vignettes/Statues-Monumentales/128-CN-Henan-Bouddha-du-temple-du-printemps-V.jpg",
                Arrays.asList(VisualFeatures.CAPTION, VisualFeatures.READ),
                new ImageAnalysisOptions().setGenderNeutralCaption(true));



        return result.getCaption().getText();


    }

    public Monument findClosestMonument() {
        Set<String> descriptionWords = new HashSet<>(Arrays.asList(recognizeMonument().split("\\s+")));
        List<Monument> monuments = monumentRepository.findAll();
        Monument closestMonument = null;
        int maxMatchCount = 0;

        for (Monument monument : monuments) {
            Set<String> monumentWords = new HashSet<>(Arrays.asList(monument.getName().split("\\s+")));
            int matchCount = 0;
            for (String word : descriptionWords) {
                if (monumentWords.contains(word.toLowerCase())) {
                    matchCount++;
                }
            }
            if (matchCount > maxMatchCount) {
                maxMatchCount = matchCount;
                closestMonument = monument;
            }
        }


        return closestMonument;
    }

}

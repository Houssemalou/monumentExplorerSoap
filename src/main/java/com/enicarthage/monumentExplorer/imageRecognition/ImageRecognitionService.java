package com.enicarthage.monumentExplorer.imageRecognition;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ImageRecognitionService {
    public String recognizeMonument(byte[] imageBytes) throws IOException {
        ByteString imgBytes = ByteString.copyFrom(imageBytes);
        Image img = Image.newBuilder().setContent(imgBytes).build();

        Feature feat = Feature.newBuilder().setType(Feature.Type.LANDMARK_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();

        try (ImageAnnotatorClient visionClient = ImageAnnotatorClient.create()) {
            AnnotateImageResponse response = visionClient.batchAnnotateImages(List.of(request)).getResponses(0);
            if (response.hasError()) {
                System.out.printf("Error: %s\n", response.getError().getMessage());
                return null;
            }

            for (EntityAnnotation annotation : response.getLandmarkAnnotationsList()) {
                return annotation.getDescription();  // Return the recognized landmark/monument name
            }
        }

        return null;
    }
}

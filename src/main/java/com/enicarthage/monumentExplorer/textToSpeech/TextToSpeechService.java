package com.enicarthage.monumentExplorer.textToSpeech;

import com.google.cloud.texttospeech.v1.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TextToSpeechService {

    public byte[] generateSpeech(String text) throws IOException {
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();
            AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);
            return response.getAudioContent().toByteArray();
        }
    }
}


package com.enicarthage.monumentExplorer;

import com.enicarthage.monumentExplorer.dataScaraping.DataScrappingService;

import com.enicarthage.monumentExplorer.imageRecognition.ImageRecognitionService;
import com.enicarthage.monumentExplorer.monument.MonumentService;
import com.enicarthage.monumentExplorer.textToSpeech.TextToSpeechService;
import javazoom.jl.player.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
@RequiredArgsConstructor
public class MonumentExplorerApplication implements CommandLineRunner {  // Implement CommandLineRunner
	private final DataScrappingService dataScrappingService;
	private final MonumentService monumentService;
	private final ImageRecognitionService imageRecognitionService;
	private final TextToSpeechService textToSpeechService;

	public static void playAudio(String filePath) {

		try (FileInputStream fis = new FileInputStream(filePath)) {
			Player player = new Player(fis);
			player.play();

			System.out.println("Lecture du fichier audio : " + filePath);
		} catch (FileNotFoundException e) {
			System.out.println("Fichier non trouvé : " + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		SpringApplication.run(MonumentExplorerApplication.class, args);
		/*MonumentExplorerApplication.playAudio("");*/
	}

	@Override
	public void run(String... args) throws IOException {
		this.textToSpeechService.convertTextToAudio("khaled where are you my friend");
		if(this.monumentService.getAllMonuments().isEmpty()){
			dataScrappingService.scrapeMonuments();
		}

	}
}

package com.enicarthage.monumentExplorer;

import com.enicarthage.monumentExplorer.dataScaraping.DataScrappingService;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class MonumentExplorerApplication implements CommandLineRunner {  // Implement CommandLineRunner
	private final DataScrappingService dataScrappingService;

	public static void main(String[] args) {
		SpringApplication.run(MonumentExplorerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		dataScrappingService.scrapeMonuments();
	}
}

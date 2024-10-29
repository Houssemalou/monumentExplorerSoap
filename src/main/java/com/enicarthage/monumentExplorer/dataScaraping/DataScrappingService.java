package com.enicarthage.monumentExplorer.dataScaraping;

import com.enicarthage.monumentExplorer.monument.Monument;
import com.enicarthage.monumentExplorer.monument.MonumentRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataScrappingService {
    private final MonumentRepository monumentRepository;
    private final String BASE_URL = "https://www.merveilles-du-monde.com/";
    private final String TARGET_URL = BASE_URL + "Etudes/Liste-des-statues-monumentales-dans-le-Monde.php";

    public List<Monument> scrapeMonuments() {
        List<Monument> monuments = new ArrayList<>();
        int count = 0;

        try {
            Document document = Jsoup.connect(TARGET_URL).get();
            Elements monumentRows = document.select("tr");
            for (Element row : monumentRows) {
                String name = row.selectFirst("h3") != null ? row.selectFirst("h3").text() : null;
                String historicalDetails = row.selectFirst("p") != null ? row.selectFirst("p").text() : null;
                Elements puceRougeElements = row.select(".PuceRouge");
                String country = puceRougeElements.size() >= 3 ? puceRougeElements.get(4).text().replace("Pays : ", "") : "Unknown";
                String imageUrl = row.selectFirst("img") != null ? row.selectFirst("img").attr("src") : null;
                if (name != null && historicalDetails != null && imageUrl != null) {
                    Monument monument = Monument.builder()
                            .name(name)
                            .country(country)
                            .imageUrl(imageUrl)
                            .historicalDetails(historicalDetails)
                            .build();
                    monuments.add(monument);
                    count++;

                }
            }

            monumentRepository.saveAll(monuments);
            System.out.println(count + " monuments scraped successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return monuments;
    }
}

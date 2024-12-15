package com.enicarthage.monumentExplorer.monument;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonumentService {
    private final MonumentRepository monumentRepository;

    public List<Monument>  getAllMonuments() {
        return monumentRepository.findAll();
    }

    public Monument getMonumentById(int id) {
        return monumentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Monument not found"));
    }

    public List<Monument>  getAllMonumentsByCountry(String country) {
        return monumentRepository.findByCountry(country);
    }

    /*public List<Monument>  getAllMonumentsByNameContaining(String name) {
        return monumentRepository.findByNameContaining(name);
    }*/

}

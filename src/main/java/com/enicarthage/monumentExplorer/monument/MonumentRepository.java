package com.enicarthage.monumentExplorer.monument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonumentRepository extends JpaRepository<Monument, Integer> {
    List<Monument> findByCountry(String country);
    Monument findByNameContainingIgnoreCase(String name);
    List<Monument> findByNameContaining(String name);
}

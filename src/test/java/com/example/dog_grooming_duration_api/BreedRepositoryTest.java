package com.example.dog_grooming_duration_api;

import com.example.dog_grooming_duration_api.entities.Breed;
import com.example.dog_grooming_duration_api.repositories.BreedRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class BreedRepositoryTest {

    @Autowired
    private BreedRepository breedRepository;

    @Test
    void shouldFindBreedById() {
        var breed = breedRepository.findById(1L);

        assertTrue(breed.isPresent());

        var extractedBreed = breed.get();
        assertEquals("Cocker Spaniel", extractedBreed.getName());
        assertEquals("COCKER_SPANIEL", extractedBreed.getCode());
        assertTrue(extractedBreed.isActive());
    }

    @Test
    void findByCodeReturnsMatchingBreed() {

        Optional<Breed> result = breedRepository.findByCode("COCKER_SPANIEL");

        assertTrue(result.isPresent());
        assertEquals("COCKER_SPANIEL", result.get().getCode());
        assertEquals("Cocker Spaniel", result.get().getName());
    }
}
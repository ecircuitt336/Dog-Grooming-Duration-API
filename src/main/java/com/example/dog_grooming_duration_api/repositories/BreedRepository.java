package com.example.dog_grooming_duration_api.repositories;

import com.example.dog_grooming_duration_api.entities.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BreedRepository extends JpaRepository<Breed, Long> {
    Optional<Breed> findByCode(String code);
}

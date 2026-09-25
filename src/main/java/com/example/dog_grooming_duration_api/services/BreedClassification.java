package com.example.dog_grooming_duration_api.services;

import com.example.dog_grooming_duration_api.entities.Breed;
import com.example.dog_grooming_duration_api.enums.BreedCategory;
import org.springframework.stereotype.Component;

/**
 * This class defines the complexity of each breed's coat.
 * The complexity of the coat affects the estimated duration of a dog groom.
 */

@Component
public class BreedClassification {

    public BreedCategory getCategory(Breed breed) {
        switch (breed.getCode())
        {
            case "COCKER_SPANIEL":
                return BreedCategory.COMPLEX_COAT;
            case "LABRADOR_RETRIEVER":
                return BreedCategory.STANDARD;
            default:
                throw new IllegalArgumentException("No classification exists for breed " + breed.getCode());
        }
    }
}

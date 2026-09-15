package com.example.dog_grooming_duration_api;

import com.example.dog_grooming_duration_api.entities.Breed;
import com.example.dog_grooming_duration_api.enums.BreedCategory;
import com.example.dog_grooming_duration_api.services.BreedClassification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BreedClassificationTest {

    private final BreedClassification breedClassification =
            new BreedClassification();

    @Test
    void getCockerSpanielCategory() {

        Breed breed = mock(Breed.class);

        when(breed.getCode()).thenReturn("COCKER_SPANIEL");

        BreedCategory result = breedClassification.getCategory(breed);

        assertEquals(BreedCategory.COMPLEX_COAT, result);
    }

    @Test
    void getUnknownBreedCategoryThrowsException() {

        Breed breed = mock(Breed.class);

        when(breed.getCode()).thenReturn("UNKNOWN_BREED");

        assertThrows(
                IllegalArgumentException.class,
                () -> breedClassification.getCategory(breed)
        );
    }

    @Test
    void getLabradorRetrieverCategory() {

        Breed breed = mock(Breed.class);

        when(breed.getCode()).thenReturn("LABRADOR_RETRIEVER");

        BreedCategory result = breedClassification.getCategory(breed);

        assertEquals(BreedCategory.STANDARD, result);
    }
}
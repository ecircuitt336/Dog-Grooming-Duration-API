package com.example.dog_grooming_duration_api;

import com.example.dog_grooming_duration_api.entities.Breed;
import com.example.dog_grooming_duration_api.enums.*;
import com.example.dog_grooming_duration_api.services.BreedClassification;
import com.example.dog_grooming_duration_api.services.EstimationResult;
import com.example.dog_grooming_duration_api.services.RulesBasedEstimator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RulesBasedEstimatorTest {

    private final BreedClassification breedClassification =
            mock(BreedClassification.class);

    private final RulesBasedEstimator estimator =
            new RulesBasedEstimator(breedClassification);

    @Test
    void bathAndDryWithBaselineInputsReturns45Minutes() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.SHORT,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.OKAY,
                Service.BATH_AND_DRY,
                new BigDecimal("6.0")
        );

        assertEquals(45, result.getEstimatedMinutes());
    }

    @Test
    void longCoatAdds20Minutes() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.LONG,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.OKAY,
                Service.BATH_AND_DRY,
                new BigDecimal("6.0")
        );

        assertEquals(65, result.getEstimatedMinutes());
    }

    @Test
    void curlyCoatAdds10Minutes() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.SHORT,
                CoatTexture.CURLY,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.OKAY,
                Service.BATH_AND_DRY,
                new BigDecimal("6.0")
        );

        assertEquals(55, result.getEstimatedMinutes());
    }

    @Test
    void doubleCoatAdds15Minutes() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.SHORT,
                CoatTexture.SMOOTH,
                CoatStructure.DOUBLE,
                MattingSeverity.NONE,
                Behaviour.OKAY,
                Service.BATH_AND_DRY,
                new BigDecimal("6.0")
        );

        assertEquals(60, result.getEstimatedMinutes());
    }

    @Test
    void moderateMattingAdds25Minutes() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.SHORT,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.MODERATE,
                Behaviour.OKAY,
                Service.BATH_AND_DRY,
                new BigDecimal("6.0")
        );

        assertEquals(70, result.getEstimatedMinutes());
    }

    @Test
    void badBehaviourAdds15Minutes() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.SHORT,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.BAD,
                Service.BATH_AND_DRY,
                new BigDecimal("6.0")
        );

        assertEquals(60, result.getEstimatedMinutes());
    }

    @Test
    void weightBetween20And30KgAdds10Minutes() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("25.0"),
                CoatLength.SHORT,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.OKAY,
                Service.BATH_AND_DRY,
                new BigDecimal("6.0")
        );

        assertEquals(55, result.getEstimatedMinutes());
    }

    @Test
    void lessThanOneYearExperienceAdds15Minutes() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.SHORT,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.OKAY,
                Service.BATH_AND_DRY,
                new BigDecimal("0.5")
        );

        assertEquals(60, result.getEstimatedMinutes());
    }

    @Test
    void complexCoatBreedAdds10Minutes() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.COMPLEX_COAT);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.SHORT,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.OKAY,
                Service.BATH_AND_DRY,
                new BigDecimal("6.0")
        );

        assertEquals(55, result.getEstimatedMinutes());
    }

    @Test
    void baselineEstimateProducesProvisionalPredictionBounds() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.SHORT,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.OKAY,
                Service.BATH_AND_DRY,
                new BigDecimal("6.0")
        );

        assertEquals(45, result.getEstimatedMinutes());
        assertEquals(38, result.getLowerBoundMinutes());
        assertEquals(52, result.getUpperBoundMinutes());
    }

    @Test
    void fullGroomEstimateProducesCorrectProvisionalPredictionBounds() {

        Breed breed = mock(Breed.class);

        when(breedClassification.getCategory(breed))
                .thenReturn(BreedCategory.STANDARD);

        EstimationResult result = estimator.estimate(
                breed,
                new BigDecimal("10.0"),
                CoatLength.SHORT,
                CoatTexture.SMOOTH,
                CoatStructure.SINGLE,
                MattingSeverity.NONE,
                Behaviour.OKAY,
                Service.FULL_GROOM_AND_CLIP,
                new BigDecimal("6.0")
        );

        assertEquals(120, result.getEstimatedMinutes());
        assertEquals(102, result.getLowerBoundMinutes());
        assertEquals(138, result.getUpperBoundMinutes());
    }
}
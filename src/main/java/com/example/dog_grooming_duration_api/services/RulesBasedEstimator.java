package com.example.dog_grooming_duration_api.services;

import com.example.dog_grooming_duration_api.entities.Breed;
import com.example.dog_grooming_duration_api.enums.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RulesBasedEstimator {

    private final BreedClassification breedClassification;

    public RulesBasedEstimator(BreedClassification breedClassification) {
        this.breedClassification = breedClassification;
    }

    public EstimationResult estimate(
            Breed breed,
            BigDecimal weightKg,
            CoatLength coatLength,
            CoatTexture coatTexture,
            CoatStructure coatStructure,
            MattingSeverity mattingSeverity,
            Behaviour behaviour,
            Service service,
            BigDecimal groomerExperienceYears
    ) {
        int estimatedMinutes = switch (service) {
            case BATH_AND_DRY -> 45;
            case TIDY_UP -> 60;
            case FULL_GROOM_AND_CLIP -> 120;
            case FULL_GROOM_AND_HAND_STRIP -> 150;
        };

        int coatLengthAdjustment = switch (coatLength) {
            case SHORT -> 0;
            case MEDIUM -> 10;
            case LONG -> 20;
        };

        estimatedMinutes += coatLengthAdjustment;

        int coatTextureAdjustment = switch (coatTexture) {
            case SMOOTH -> 0;
            case WAVY -> 5;
            case CURLY -> 10;
            case WIRY -> 10;
            case SILKY -> 5;
            case ROUGH -> 10;
        };

        estimatedMinutes += coatTextureAdjustment;

        int coatStructureAdjustment = switch (coatStructure) {
            case SINGLE -> 0;
            case DOUBLE -> 15;
        };

        estimatedMinutes += coatStructureAdjustment;

        int mattingAdjustment = switch (mattingSeverity) {
            case NONE -> 0;
            case MILD -> 10;
            case MODERATE -> 25;
            case SEVERE -> 45;
        };

        estimatedMinutes += mattingAdjustment;

        int behaviourAdjustment = switch (behaviour) {
            case VERY_GOOD -> -10;
            case GOOD -> -5;
            case OKAY -> 0;
            case BAD -> 15;
            case VERY_BAD -> 30;
        };

        estimatedMinutes += behaviourAdjustment;

        int weightAdjustment;
        if (weightKg.compareTo(new BigDecimal("5")) <= 0) {
            weightAdjustment = -5;
        } else if (weightKg.compareTo(new BigDecimal("10")) <= 0) {
            weightAdjustment = 0;
        } else if (weightKg.compareTo(new BigDecimal("20")) <= 0) {
            weightAdjustment = 5;
        } else if (weightKg.compareTo(new BigDecimal("30")) <= 0) {
            weightAdjustment = 10;
        } else if (weightKg.compareTo(new BigDecimal("40")) <= 0) {
            weightAdjustment = 15;
        } else {
            weightAdjustment = 20;
        }

        estimatedMinutes += weightAdjustment;

        int experienceAdjustment;

        if (groomerExperienceYears.compareTo(new BigDecimal("1")) < 0) {
            experienceAdjustment = 15;
        } else if (groomerExperienceYears.compareTo(new BigDecimal("2")) <= 0) {
            experienceAdjustment = 10;
        } else if (groomerExperienceYears.compareTo(new BigDecimal("5")) <= 0) {
            experienceAdjustment = 5;
        } else {
            experienceAdjustment = 0;
        }

        estimatedMinutes += experienceAdjustment;

        BreedCategory breedCategory = breedClassification.getCategory(breed);

        int breedAdjustment = switch (breedCategory) {
            case SIMPLE_COAT -> -5;
            case STANDARD -> 0;
            case COMPLEX_COAT -> 10;
            case SPECIALIST -> 15;
        };

        estimatedMinutes += breedAdjustment;

        int lowerBoundMinutes = (int) Math.round(estimatedMinutes * 0.85);
        int upperBoundMinutes = (int) Math.round(estimatedMinutes * 1.15);

        return new EstimationResult(estimatedMinutes, lowerBoundMinutes, upperBoundMinutes);
    }
}

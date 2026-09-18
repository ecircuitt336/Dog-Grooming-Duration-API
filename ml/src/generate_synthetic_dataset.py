import random
import pandas as pd

random.seed(42)

BREEDS = [
    "COCKER_SPANIEL",
    "LABRADOR_RETRIEVER",
]

COAT_LENGTHS = [
    "SHORT",
    "MEDIUM",
    "LONG",
]

COAT_TEXTURES = [
    "SMOOTH",
    "WAVY",
    "CURLY",
    "WIRY",
    "SILKY",
    "ROUGH",
]

COAT_STRUCTURES = [
    "SINGLE",
    "DOUBLE",
]

MATTING_SEVERITIES = [
    "NONE",
    "MILD",
    "MODERATE",
    "SEVERE",
]

BEHAVIOURS = [
    "VERY_BAD",
    "BAD",
    "OKAY",
    "GOOD",
    "VERY_GOOD",
]

SERVICES = [
    "FULL_GROOM_AND_CLIP",
    "FULL_GROOM_AND_HAND_STRIP",
    "TIDY_UP",
    "BATH_AND_DRY",
]

NUMBER_OF_ROWS = 500
OUTPUT_PATH = "data/ml/synthetic_development_appointments.csv"

def generate_appointment():
    return {
        "breed": random.choice(BREEDS),
        "weight_kg": round(random.uniform(1, 40), 2),
        "coat_length": random.choice(COAT_LENGTHS),
        "coat_texture": random.choice(COAT_TEXTURES),
        "coat_structure": random.choice(COAT_STRUCTURES),
        "matting_severity": random.choice(MATTING_SEVERITIES),
        "behaviour": random.choice(BEHAVIOURS),
        "service": random.choice(SERVICES),
        "groomer_experience_years": round(random.uniform(0, 15), 1),
    }

def calculate_expected_duration(appointment):
    service_durations = {
        "BATH_AND_DRY": 45,
        "TIDY_UP": 60,
        "FULL_GROOM_AND_CLIP": 120,
        "FULL_GROOM_AND_HAND_STRIP": 150,
    }

    coat_length_durations = {
        "SHORT": 0,
        "MEDIUM": 10,
        "LONG": 20,
    }

    coat_texture_durations = {
        "SMOOTH": 0,
        "WAVY": 5,
        "CURLY": 10,
        "WIRY": 10,
        "SILKY": 5,
        "ROUGH": 10,
    }

    coat_structure_durations = {
        "SINGLE": 0,
        "DOUBLE": 15,
    }

    matting_durations = {
        "NONE": 0,
        "MILD": 10,
        "MODERATE": 25,
        "SEVERE": 45,
    }

    behaviour_durations = {
        "VERY_BAD": 30,
        "BAD": 15,
        "OKAY": 0,
        "GOOD": -5,
        "VERY_GOOD": -10,
    }

    breed_durations = {
        "COCKER_SPANIEL": 10,
        "LABRADOR_RETRIEVER": 0,
    }

    duration = service_durations[appointment["service"]]
    duration += coat_length_durations[appointment["coat_length"]]
    duration += coat_texture_durations[appointment["coat_texture"]]
    duration += coat_structure_durations[appointment["coat_structure"]]
    duration += matting_durations[appointment["matting_severity"]]
    duration += behaviour_durations[appointment["behaviour"]]
    duration += breed_durations[appointment["breed"]]

    weight = appointment["weight_kg"]

    if weight <= 5:
        duration -= 5
    elif weight <= 10:
        duration += 0
    elif weight <= 20:
        duration += 5
    elif weight <= 30:
        duration += 10
    elif weight <= 40:
        duration += 15
    else:
        duration += 20

    experience = appointment["groomer_experience_years"]

    if experience < 1:
        duration += 15
    elif experience <= 2:
        duration += 10
    elif experience <= 5:
        duration += 5

    return duration

def generate_actual_duration(appointment):
    expected_duration = calculate_expected_duration(appointment)
    noise = random.gauss(0, 15)

    actual_duration = expected_duration + noise

    return max(1, round(actual_duration))

def generate_row():
    appointment = generate_appointment()
    actual_duration = generate_actual_duration(appointment)

    appointment["actual_duration_minutes"] = actual_duration

    return appointment

rows = []

for _ in range(NUMBER_OF_ROWS):
    rows.append(generate_row())

df = pd.DataFrame(rows)
df.to_csv(OUTPUT_PATH, index=False)
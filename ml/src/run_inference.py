from model_persistence import load_model
import pandas as pd

model = load_model("data/ml/models/linear_regression_pipeline.joblib")

appointment = pd.DataFrame([{
    "breed": "COCKER_SPANIEL",
    "weight_kg": 12.5,
    "coat_length": "LONG",
    "coat_texture": "WAVY",
    "coat_structure": "SINGLE",
    "matting_severity": "MODERATE",
    "behaviour": "GOOD",
    "service": "FULL_GROOM_AND_CLIP",
    "groomer_experience_years": 2.0
}])

duration = model.predict(appointment)

print(duration)
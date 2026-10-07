import pandas as pd
import numpy as np

from sklearn.compose import ColumnTransformer
from sklearn.model_selection import (
    train_test_split,
    KFold,
    cross_val_score
)
from sklearn.preprocessing import OneHotEncoder
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import (
    mean_absolute_error,
    root_mean_squared_error,
    r2_score
)
from sklearn.pipeline import Pipeline
from pathlib import Path
from model_persistence import save_model

# Loads the dataset
csv_path = "data/ml/synthetic_development_appointments.csv"
model_path = Path("data/ml/models/linear_regression_pipeline.joblib")
df = pd.read_csv(csv_path)

# Target is what the ML model needs to predict
target = df["actual_duration_minutes"]

# Separates features from the target
features = df.drop(columns=["actual_duration_minutes"])

# Characteristics that have numerical values
numerical_features = [
    "weight_kg",
    "groomer_experience_years"
]

# Characteristics that have categorical values
categorical_features = [
    "breed",
    "coat_length",
    "coat_texture",
    "coat_structure",
    "matting_severity",
    "behaviour",
    "service"
]

def create_preprocessor():
    # Converts categorical features into numerical one-hot encoded columns.
    # handle_unknown="ignore" means rather than crashing, the encoder ignores any unkown category.
    categorical_encoder = OneHotEncoder(handle_unknown="ignore")

    # Applies one-hot encoding to categorical features while leaving numerical features unchanged.
    return ColumnTransformer(
        transformers=[
            ("categorical", categorical_encoder, categorical_features),
            ("numerical", "passthrough", numerical_features)
        ]
    )

# Combines preprocessing and Linear Regression into a single pipeline.
model_pipeline = Pipeline(
    steps=[
        ("preprocessor", create_preprocessor()),
        ("model", LinearRegression())
    ]
)

# Splits the dataset into 80% training data and 20% testing data.
X_train, X_test, y_train, y_test = train_test_split(
    features,
    target,
    test_size=0.2,
    random_state=42
)

# Configures 5-fold cross-validation.
# This divides the training data into 5 folds. It runs 5 times, with a different fold being used to validate each time.
# Shuffle randomises the data before creating the folds
kf = KFold(
    n_splits=5,
    shuffle=True,
    random_state=42
)

cv_pipeline = Pipeline(
    steps=[
        ("preprocessor", create_preprocessor()),
        ("model", LinearRegression())
    ]
)

cv_mae_scores = -cross_val_score(
    cv_pipeline,
    X_train,
    y_train,
    cv=kf,
    scoring="neg_mean_absolute_error"
)

for fold, score in enumerate(cv_mae_scores, start=1):
    print(f"Fold {fold} MAE: {score:.2f}")

# Calculate mean and standard deviation
mean_cv_mae = np.mean(cv_mae_scores)
std_cv_mae = np.std(cv_mae_scores)

print("Mean CV MAE:", mean_cv_mae)
print("CV MAE standard deviation:", std_cv_mae)

# Trains the final Linear Regression model on all 80% of training data
model_pipeline.fit(X_train, y_train)
predictions = model_pipeline.predict(X_test)

# Inspecting first 10 predictions for debugging
for actual, predicted in zip(y_test.head(10), predictions[:10]):
    print(f"Actual: {actual} minutes | Predicted: {predicted:.1f} minutes")

errors = y_test - predictions

for actual, predicted, error in zip(y_test.head(10), predictions[:10], errors[:10]):
    print(
        f"Actual: {actual} | "
        f"Predicted: {predicted:.1f} | "
        f"Error: {error:.1f}"
    )

# Whether the model tends to overestimate or underestimate
mean_error = errors.mean()
print("Mean signed error:", mean_error)

# Typical absolute prediction error
mae = mean_absolute_error(y_test, predictions)

print("MAE:", mae)

# Error with greater emphasis on larger mistakes
rmse = root_mean_squared_error(y_test, predictions)

print("RMSE:", rmse)

# Proportion of target variation explained relative to a mean-predition baseline
r2 = r2_score(y_test, predictions)
print("R^2:", r2)

save_model(model_pipeline, model_path)
print(f"Model saved to: {model_path}")

# Random Forest Pipeline
random_forest_pipeline = Pipeline(
    steps=[
        ("preprocessor", create_preprocessor()),
        (
            "model",
            RandomForestRegressor(
                n_estimators=100,
                random_state=42
            )
        )
    ]
)

# Train the Random Forest
random_forest_pipeline.fit(X_train, y_train)

# Make predictions
random_forest_predictions = random_forest_pipeline.predict(X_test)

# Get Random Forest metrics
random_forest_mae = mean_absolute_error(
    y_test,
    random_forest_predictions
)

random_forest_rmse = root_mean_squared_error(
    y_test,
    random_forest_predictions
)

random_forest_r2 = r2_score(
    y_test,
    random_forest_predictions
)

print("Random Forest MAE:", random_forest_mae)
print("Random Forest RMSE:", random_forest_rmse)
print("Random Forest R²:", random_forest_r2)

# Comparison table: Linear Regression VS Random forest
model_comparison = pd.DataFrame({
    "Model": [
        "Linear Regression",
        "Random Forest"
    ],
    "MAE": [
        mae,
        random_forest_mae
    ],
    "RMSE": [
        rmse,
        random_forest_rmse
    ],
    "R²": [
        r2,
        random_forest_r2
    ]
})

print("\nModel comparison:")
print(model_comparison)

# Compare individual predictions for debugging
for actual, linear_prediction, random_forest_prediction in zip(
    y_test.head(10),
    predictions[:10],
    random_forest_predictions[:10]
):
    print(
        f"Actual: {actual} | "
        f"Linear Regression: {linear_prediction:.1f} | "
        f"Random Forest: {random_forest_prediction:.1f}"
    )

# Creates a table for analysing where the Linear Regression model makes errors.
error_analysis = pd.DataFrame({
    "actual": y_test,
    "predicted": predictions,
    "error": errors,
    "absolute_error": abs(errors)
})

print(error_analysis.head(10))

# Group actual durations into ranges to analyse model error by appointment length
error_analysis["actual_duration_bin"] = pd.cut(
    error_analysis["actual"],
    bins=[0, 90, 150, 210, float("inf")],
    labels=["Short", "Medium", "Long", "Very Long"]
)

print(
    error_analysis.groupby("actual_duration_bin", observed=True)[
        ["error", "absolute_error"]
    ].mean()
)

error_analysis["service"] = X_test["service"].values

print(
    error_analysis.groupby("service")["absolute_error"]
    .mean()
)

error_analysis["behaviour"] = X_test["behaviour"].values

print(
    error_analysis.groupby("behaviour")["absolute_error"]
    .mean()
)
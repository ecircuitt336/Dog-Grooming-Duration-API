import pandas as pd

from sklearn.compose import ColumnTransformer
from sklearn.model_selection import train_test_split
from sklearn.model_selection import KFold
from sklearn.preprocessing import OneHotEncoder
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import root_mean_squared_error
from sklearn.metrics import r2_score

import numpy as np

kf = KFold(
    n_splits=5,
    shuffle=True,
    random_state=42
)

model = LinearRegression()

csv_path = "data/ml/synthetic_development_appointments.csv"

df = pd.read_csv(csv_path)

target = df["actual_duration_minutes"]

numerical_features = [
    "weight_kg",
    "groomer_experience_years"
]

categorical_features = [
    "breed",
    "coat_length",
    "coat_texture",
    "coat_structure",
    "matting_severity",
    "behaviour",
    "service"
]

categorical_encoder = OneHotEncoder(handle_unknown="ignore")

preprocessor = ColumnTransformer(
    transformers=[
        ("categorical", categorical_encoder, categorical_features),
        ("numerical", "passthrough", numerical_features)
    ]
)

features = df.drop(columns=["actual_duration_minutes"])

X_train, X_test, y_train, y_test = train_test_split(
    features,
    target,
    test_size=0.2,
    random_state=42
)

for fold, (train_indices, validation_indices) in enumerate(kf.split(X_train), start=1):
    print(
        f"Fold {fold}: "
        f"training samples = {len(train_indices)}, "
        f"validation samples = {len(validation_indices)}"
    )

cv_mae_scores = []

for fold, (train_indices, validation_indices) in enumerate(
    kf.split(X_train),
    start=1
):
    X_fold_train = X_train.iloc[train_indices]
    X_fold_validation = X_train.iloc[validation_indices]

    y_fold_train = y_train.iloc[train_indices]
    y_fold_validation = y_train.iloc[validation_indices]

    preprocessor_fold = ColumnTransformer(
        transformers=[
            ("categorical", OneHotEncoder(handle_unknown="ignore"), categorical_features),
            ("numerical", "passthrough", numerical_features)
        ]
    )

    X_fold_train_transformed = preprocessor_fold.fit_transform(X_fold_train)
    X_fold_validation_transformed = preprocessor_fold.transform(X_fold_validation)

    model_fold = LinearRegression()

    model_fold.fit(
        X_fold_train_transformed,
        y_fold_train
    )

    fold_predictions = model_fold.predict(
        X_fold_validation_transformed
    )

    fold_mae = mean_absolute_error(
        y_fold_validation,
        fold_predictions
    )

    cv_mae_scores.append(fold_mae)

    print(f"Fold {fold} MAE: {fold_mae:.2f}")

mean_cv_mae = np.mean(cv_mae_scores)
std_cv_mae = np.std(cv_mae_scores)

print("Mean CV MAE:", mean_cv_mae)
print("CV MAE standard deviation:", std_cv_mae)

X_train_transformed = preprocessor.fit_transform(X_train)
X_test_transformed = preprocessor.transform(X_test)

model.fit(X_train_transformed, y_train)

predictions = model.predict(X_test_transformed)

for actual, predicted in zip(y_test.head(10), predictions[:10]):
    print(f"Actual: {actual} minutes | Predicted: {predicted:.1f} minutes")

errors = y_test - predictions

for actual, predicted, error in zip(y_test.head(10), predictions[:10], errors[:10]):
    print(
        f"Actual: {actual} | "
        f"Predicted: {predicted:.1f} | "
        f"Error: {error:.1f}"
    )

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

random_forest_model = RandomForestRegressor(
    n_estimators=100,
    random_state=42
)

random_forest_model.fit(X_train_transformed, y_train)

random_forest_predictions = random_forest_model.predict(X_test_transformed)

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

random_forest_errors = y_test - random_forest_predictions
random_forest_absolute_errors = abs(random_forest_errors)

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

error_analysis = pd.DataFrame({
    "actual": y_test,
    "predicted": predictions,
    "error": errors,
    "absolute_error": abs(errors)
})

print(error_analysis.head(10))

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
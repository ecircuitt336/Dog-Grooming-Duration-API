import pandas as pd

csv_path = "data/ml/synthetic_development_appointments.csv"

df = pd.read_csv(csv_path)

print("Shape:")
print(df.shape)

print("\nColumns:")
print(df.columns.tolist())

print("\nData types:")
print(df.dtypes)

print("\nDataset:")
print(df.to_string(index=False))

print("\nMissing values:")
print(df.isnull().sum())

categorical_columns = [
    "breed",
    "coat_length",
    "coat_texture",
    "coat_structure",
    "matting_severity",
    "behaviour",
    "service"
]

print("\nUnique values:")
for column in categorical_columns:
    print(f"\n{column}:")
    print(df[column].value_counts())

print("\nNumeric summary:")
print(df.describe())
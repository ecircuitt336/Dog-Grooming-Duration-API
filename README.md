# Dog Grooming Duration API

A REST API for estimating how long a dog grooming appointment will take, using dog characteristics, grooming requirements, service type, and groomer experience.

The project combines a **Java/Spring Boot REST API**, **PostgreSQL persistence**, and a **Python machine learning pipeline**. It is being developed as an open-source software engineering and machine learning portfolio project.

## Overview

Accurately estimating grooming duration can help independent dog groomers and grooming salons schedule appointments more effectively.

The API currently provides a rules-based duration estimate using nine input features:

* Breed
* Weight
* Coat length
* Coat texture
* Coat structure
* Matting severity
* Behaviour
* Service
* Groomer experience

Completed appointments record their actual duration. These completed records form the dataset used for the machine learning component.

The machine learning model is being developed to learn from historical grooming observations and eventually replace or complement the initial rules-based estimator.

## Current Status

The Java API, PostgreSQL persistence, appointment completion workflow, ML dataset export pipeline, and Python machine learning development workflow are implemented.

Real grooming data is currently being collected. Because the initial real-world dataset is small, a separate synthetic dataset is being used to develop and test the machine learning pipeline.

The current synthetic development workflow includes:

* Categorical and numerical feature preprocessing
* One-hot encoding
* Linear Regression baseline
* Random Forest comparison
* MAE, RMSE and R² evaluation
* Residual and error analysis
* Five-fold cross-validation

Synthetic results are used to validate the ML development workflow and **are not treated as evidence of real-world model performance**.

### Roadmap

* [x] Appointment persistence
* [x] Rules-based duration estimator
* [x] Actual duration recording
* [x] Completed appointment dataset export
* [x] Python dataset inspection
* [x] Initial data quality checks
* [x] Synthetic ML development dataset
* [x] Synthetic dataset validation
* [x] ML preprocessing
* [x] Regression model training
* [x] Initial model evaluation
* [x] Error analysis
* [x] Five-fold cross-validation
* [~] Real-world data collection
* [ ] Exploratory data analysis on real-world data
* [ ] ML pipeline refactor using scikit-learn Pipeline
* [ ] Model comparison and selection using real-world data
* [ ] Model persistence
* [ ] Python inference service
* [ ] Java/Python integration
* [ ] Production deployment

## Architecture

The project separates the application/API layer from the machine learning workflow.

```text
                         ┌─────────────────────┐
                         │       Client        │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │ Java / Spring Boot  │
                         │      REST API       │
                         └──────────┬──────────┘
                                    │
                    ┌───────────────┴───────────────┐
                    │                               │
                    ▼                               ▼
          ┌─────────────────┐             ┌─────────────────┐
          │ Rules Estimator │             │   PostgreSQL    │
          └─────────────────┘             │    Database     │
                                          └────────┬────────┘
                                                   │
                                                   │ Completed
                                                   │ appointments
                                                   ▼
                                          ┌─────────────────┐
                                          │    ML Dataset   │
                                          │      (CSV)      │
                                          └────────┬────────┘
                                                   │
                                                   ▼
                                          ┌─────────────────┐
                                          │ Python / pandas │
                                          │ / scikit-learn  │
                                          └─────────────────┘
```

The intended architecture keeps responsibilities separate:

* **Java/Spring Boot** — API, validation, business logic and persistence
* **PostgreSQL** — appointment and breed data
* **Python** — data analysis, preprocessing, model training and evaluation
* **scikit-learn** — machine learning implementation

The Python machine learning workflow is currently used for development and evaluation. Model inference will be integrated with the Java API in a later phase.

## API

### `GET /health`

Returns the API health status.

### `POST /api/v1/estimates`

Creates a grooming appointment and returns a duration estimate.

The response contains:

* `estimatedMinutes`
* `lowerBoundMinutes`
* `upperBoundMinutes`

The current bounds are provisional ±15% estimates and are not statistically validated prediction intervals.

### `POST /api/v1/appointments/{id}/actual-duration`

Records the actual grooming duration for a completed appointment.

This data is retained for machine learning and model evaluation.

## Machine Learning

The project is being developed with a machine-learning component to estimate dog grooming appointment duration from completed appointment data.

The initial ML dataset contains the following features:

* `breed`
* `weight_kg`
* `coat_length`
* `coat_texture`
* `coat_structure`
* `matting_severity`
* `behaviour`
* `service`
* `groomer_experience_years`

The target variable is:

* `actual_duration_minutes`

Only completed appointments with a recorded actual duration are included in the ML dataset.

### Development Dataset

Real-world grooming data is being collected over time. Because a sufficiently large real dataset is not available yet, a separate synthetic dataset is used during development to build and test the ML pipeline.

The synthetic development dataset contains **500 generated appointments** and is stored at:

`data/ml/synthetic_development_appointments.csv`

The synthetic data is generated from the project's existing rules-based duration assumptions with additional random variation to simulate differences between otherwise similar grooming appointments.

**The synthetic dataset is for development and testing only. Its distribution and any machine-learning performance measured from it should not be interpreted as evidence of real-world model performance.**

The eventual model will be evaluated using real completed grooming appointments as sufficient observations become available.

### Preprocessing

The current development dataset contains both numerical and categorical features.

Numerical features:

* `weight_kg`
* `groomer_experience_years`

Categorical features:

* `breed`
* `coat_length`
* `coat_texture`
* `coat_structure`
* `matting_severity`
* `behaviour`
* `service`

Categorical features are currently encoded using **one-hot encoding**. Unknown categories are configured to be ignored during transformation.

The preprocessing is currently implemented explicitly using scikit-learn's `ColumnTransformer`. A later step will refactor preprocessing and model training into a scikit-learn `Pipeline` so that the complete transformation and model workflow can be handled consistently during cross-validation and model persistence.

### Initial Model Experiments

A **Linear Regression** model has been implemented as the initial baseline.

On the current 500-row synthetic development dataset, using an 80/20 train/test split:

| Metric | Linear Regression |
| ------ | ----------------: |
| MAE    |     11.93 minutes |
| RMSE   |     15.00 minutes |
| R²     |            0.9215 |

A **Random Forest Regressor** was also evaluated on the same split:

| Metric | Linear Regression | Random Forest |
| ------ | ----------------: | ------------: |
| MAE    |         11.93 min |     16.08 min |
| RMSE   |         15.00 min |     20.02 min |
| R²     |            0.9215 |        0.8601 |

On this synthetic dataset, Linear Regression produced lower error than the Random Forest model.

However, this comparison is **not evidence that Linear Regression is the appropriate final production model**. The synthetic target was generated using the project's additive rules-based assumptions, which naturally resemble the structure that a linear model can learn.

### Cross-Validation

Five-fold cross-validation has also been performed on the training portion of the synthetic dataset.

The Linear Regression model produced:

* **Mean CV MAE:** 12.19 minutes
* **CV MAE standard deviation:** 0.50 minutes

The five fold MAE values were:

| Fold |           MAE |
| ---- | ------------: |
| 1    | 11.96 minutes |
| 2    | 12.38 minutes |
| 3    | 11.42 minutes |
| 4    | 12.29 minutes |
| 5    | 12.92 minutes |

The relatively small variation between these folds indicates that the result was reasonably consistent across this particular synthetic dataset.

Cross-validation is currently being used to validate the ML development workflow. It does **not** establish expected real-world performance.

### Current ML Development Status

* [x] Export completed appointments from PostgreSQL
* [x] Generate ML-compatible CSV dataset
* [x] Inspect dataset with pandas
* [x] Create synthetic development dataset
* [x] Validate synthetic dataset
* [x] Preprocess categorical and numerical features
* [x] Train Linear Regression baseline
* [x] Evaluate model using MAE, RMSE and R²
* [x] Perform residual and error analysis
* [x] Compare Linear Regression with Random Forest
* [x] Perform five-fold cross-validation
* [ ] Refactor preprocessing and model into a scikit-learn Pipeline
* [ ] Evaluate models using real-world grooming data
* [ ] Select final model using real-world evidence
* [ ] Persist trained model
* [ ] Build Python inference service
* [ ] Integrate model inference with the Java API

## Technology Stack

### Backend

* Java 25
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Flyway
* Maven

### Machine Learning

* Python 3.13+
* pandas
* NumPy
* scikit-learn

### Testing

* JUnit
* Spring Boot Test
* Mockito
* PostgreSQL integration testing

## Running Locally

### Prerequisites

* Java 25
* Maven or Maven Wrapper
* Python 3.13+
* PostgreSQL

### Database

Create a PostgreSQL database named:

```text
dog_grooming_duration
```

Database configuration is supplied through environment variables rather than committing credentials to the repository.

See `.env.example` for the expected configuration.

### Python Environment

Create and activate a virtual environment:

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
```

Install the Python dependencies:

```powershell
pip install -r requirements.txt
```

### Run the Application

The application can be started using the Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will be available locally on:

```text
http://localhost:8080
```

## Testing

The project contains unit, integration, repository, controller, service, validation, and database constraint tests.

Tests can be run from IntelliJ IDEA or using the Maven Wrapper.

## Project Limitations

This project is currently a portfolio and development project rather than a production-ready commercial system.

Important limitations include:

* The real-world dataset is currently small.
* Synthetic data is used for ML pipeline development.
* Synthetic model results do not represent real-world model performance.
* The initial duration estimator is rules-based.
* The ±15% estimate bounds have not been statistically validated.
* The machine learning model has not yet been evaluated on a sufficiently large real-world dataset.
* Groomer experience is currently recorded as an input but may have limited variation in the initial real-world dataset.
* Model performance may change as more real grooming observations are collected.
* The synthetic dataset is based on assumptions defined by the project rather than observations collected from real grooming appointments.

These limitations are intentionally documented rather than presenting development results as evidence of production-level accuracy.

## Future Improvements

Potential future work includes:

* Collecting additional real-world grooming observations
* Expanding the dataset across different groomers
* Performing exploratory data analysis on real-world observations
* Refactoring the ML workflow using scikit-learn Pipelines
* Training and comparing regression models using real-world data
* Model selection based on real-world evaluation
* Model persistence and versioning
* Python inference service
* Java-to-Python integration
* Automated model evaluation
* CI/CD
* Containerisation where justified
* Improved prediction interval methodology

## Disclaimer

This API provides estimates for scheduling and planning purposes only.

It does not provide veterinary or medical advice, determine whether a dog is suitable for grooming, replace professional groomer judgement, or guarantee a grooming completion time.

## Licence

This project is open source. See the repository licence for the terms under which the project may be used.

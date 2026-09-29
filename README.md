# Dog Grooming Duration API

A REST API for estimating how long a dog grooming appointment will take based on the dog, the service being provided, and the groomer's experience.

The project is built with **Java, Spring Boot, PostgreSQL and Python**. It started as a rules-based estimator and is being extended to learn from completed grooming appointments.

The project is being developed as an open-source portfolio project, with a focus on building a complete system from data collection through to a working API.

## Overview

Estimating how long a grooming appointment will take can help independent groomers and salons plan their schedules.

The API currently takes nine pieces of information:

* Breed
* Weight
* Coat length
* Coat texture
* Coat structure
* Matting severity
* Behaviour
* Service
* Groomer experience

It first produces an estimate using a rules-based calculation. Once an appointment has been completed, the actual duration can be recorded.

Completed appointments are exported as a dataset which can then be analysed in Python. This provides the foundation for eventually replacing or improving the initial rules-based estimator.

## Current Status

The Java API, PostgreSQL database, appointment completion workflow and dataset export are working.

The Python side of the project is also being developed using a separate synthetic dataset while real grooming data is collected. The current development work covers:

* Preparing numerical and categorical data
* One-hot encoding categorical values
* Linear Regression
* Random Forest regression
* Five-fold cross-validation
* MAE, RMSE and R² evaluation
* Error analysis

The synthetic dataset is only being used to develop and test the software. Its results should not be taken as evidence of how well the system will perform on real grooming appointments.

### Roadmap

* [x] Appointment persistence
* [x] Rules-based duration estimator
* [x] Actual duration recording
* [x] Completed appointment dataset export
* [x] Python dataset inspection
* [x] Initial data quality checks
* [x] Synthetic development dataset
* [x] Synthetic dataset validation
* [x] Data preprocessing
* [x] Regression model training
* [x] Initial model evaluation
* [x] Error analysis
* [x] Five-fold cross-validation
* [x] scikit-learn Pipeline
* [~] Real-world data collection
* [ ] Exploratory analysis of real-world data
* [ ] Model comparison using real-world data
* [ ] Model selection using real-world data
* [ ] Save and load trained models
* [ ] Python inference service
* [ ] Java/Python integration
* [ ] Production deployment

## Architecture

The project keeps the main API and the Python data work separate.

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
                                          │    CSV Dataset  │
                                          └────────┬────────┘
                                                   │
                                                   ▼
                                          ┌─────────────────┐
                                          │     Python      │
                                          │ pandas /        │
                                          │ scikit-learn   │
                                          └─────────────────┘
```

The responsibilities are currently split as follows:

* **Java/Spring Boot** — REST API, validation, business logic and database access
* **PostgreSQL** — appointment and breed data
* **Python** — data analysis, preprocessing, model training and evaluation
* **scikit-learn** — data preprocessing, regression models and evaluation

The Python work is currently separate from the live API. Connecting the trained model to the API will be handled in a later stage.

## API

### `GET /health`

Returns the current health status of the API.

### `POST /api/v1/estimates`

Creates a grooming appointment and returns a duration estimate.

The response contains:

* `estimatedMinutes`
* `lowerBoundMinutes`
* `upperBoundMinutes`

The current lower and upper bounds are calculated as ±15% of the estimate. These are provisional and have not been statistically validated.

### `POST /api/v1/appointments/{id}/actual-duration`

Records the actual time taken to complete an appointment.

Completed appointments can then be included in the dataset used for analysis and model development.

## Data

The dataset exported from the application contains:

* `breed`
* `weight_kg`
* `coat_length`
* `coat_texture`
* `coat_structure`
* `matting_severity`
* `behaviour`
* `service`
* `groomer_experience_years`
* `actual_duration_minutes`

The first nine columns describe the appointment. `actual_duration_minutes` is the value the models are trying to predict.

Only completed appointments with an actual duration are exported.

Customer information and other unnecessary appointment details are deliberately excluded from the dataset.

## Python Development

Real grooming data is being collected over time. There is currently not enough real data to meaningfully train and evaluate a model, so a separate generated dataset is being used while the Python workflow is developed.

The generated dataset contains **500 appointments**:

```text
data/ml/synthetic_development_appointments.csv
```

The generated durations are based on the existing rules used by the API, with additional random variation.

This makes the dataset useful for testing the data-processing and model-training code, but it does not represent real grooming appointments.

### Preprocessing

The data contains both numerical and categorical values.

Numerical values:

* `weight_kg`
* `groomer_experience_years`

Categorical values:

* `breed`
* `coat_length`
* `coat_texture`
* `coat_structure`
* `matting_severity`
* `behaviour`
* `service`

Categorical values are converted into numerical columns using one-hot encoding.

The preprocessing and model are contained in a scikit-learn `Pipeline`. This means the same preprocessing steps can be applied consistently when training, testing and eventually using the saved model.

### Models

Two regression models have currently been tested:

* Linear Regression
* Random Forest Regressor

Using an 80/20 split of the 500-row synthetic dataset, the results were:

| Metric | Linear Regression | Random Forest |
| ------ | ----------------: | ------------: |
| MAE    |         11.93 min |     16.08 min |
| RMSE   |         15.00 min |     20.02 min |
| R²     |            0.9215 |        0.8601 |

On this particular synthetic dataset, Linear Regression produced smaller errors.

This result should not be used to decide which model will be used by the finished application. The synthetic durations were generated from the project's existing additive rules, so the dataset is naturally suited to a model such as Linear Regression.

The models will be compared again once enough real appointment data has been collected.

### Cross-Validation

Five-fold cross-validation has also been used with the Linear Regression model.

The results on the synthetic training data were:

| Fold |       MAE |
| ---- | --------: |
| 1    | 11.96 min |
| 2    | 12.38 min |
| 3    | 11.42 min |
| 4    | 12.29 min |
| 5    | 12.92 min |

Mean MAE:

**12.19 minutes**

Standard deviation:

**0.50 minutes**

The results were reasonably consistent across the five folds of this particular dataset.

Again, these numbers are useful for checking that the development workflow behaves as expected, but they do not represent expected performance on real appointments.

## Technology Stack

### Backend

* Java 25
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Flyway
* Maven

### Data and modelling

* Python 3.13+
* pandas
* NumPy
* scikit-learn

### Testing

* JUnit
* Spring Boot Test
* Mockito
* PostgreSQL integration tests

## Running Locally

### Prerequisites

* Java 25
* Maven or the included Maven Wrapper
* Python 3.13+
* PostgreSQL

### Database

Create a PostgreSQL database named:

```text
dog_grooming_duration
```

Database credentials are supplied through environment variables rather than committed to the repository.

See `.env.example` for the required configuration.

### Python Environment

Create a virtual environment:

```powershell
python -m venv .venv
```

Activate it:

```powershell
.\.venv\Scripts\Activate.ps1
```

Install the dependencies:

```powershell
pip install -r requirements.txt
```

### Run the API

Using the Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

## Testing

The project contains tests covering areas including:

* Domain logic
* Validation
* Controllers
* Services
* Repositories
* Database constraints
* Dataset export
* Integration between the application and PostgreSQL

Tests can be run from IntelliJ IDEA or with the Maven Wrapper.

## Project Limitations

This is currently a portfolio and development project rather than a production-ready commercial service.

Some important limitations are:

* The real-world dataset is still small.
* Generated data is being used to develop the Python workflow.
* Results from the generated data do not represent real-world performance.
* The current estimator is based on manually defined rules.
* The ±15% bounds have not been statistically validated.
* The models have not yet been tested on a sufficiently large real-world dataset.
* Groomer experience may have limited variation in the initial real-world data.
* Model performance may change substantially as more observations are collected.
* The generated dataset is based on assumptions made during development rather than real grooming observations.

These limitations are kept explicit so that development results are not presented as production results.

## Future Work

The main areas of future development are:

* Continue collecting real grooming appointments
* Analyse the real-world dataset
* Check for missing values and unusual observations
* Compare different regression models using real data
* Select a model based on real-world results
* Save and load the trained model
* Build a Python service for making predictions
* Connect the Python service to the Java API
* Improve the way prediction ranges are calculated
* Add automated model evaluation
* Add CI/CD
* Consider containerisation if it becomes useful

## Disclaimer

This API provides estimates for scheduling and planning purposes only.

It does not provide veterinary or medical advice, determine whether a dog is suitable for grooming, replace professional groomer judgement, or guarantee a grooming completion time.

## Licence

This project is open source. See the repository licence for the terms under which the project may be used.

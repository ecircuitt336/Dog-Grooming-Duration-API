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

The machine learning model will eventually learn from historical grooming observations and replace or complement the initial rules-based estimator.

## Current Status

The Java API, PostgreSQL persistence, appointment completion workflow, ML dataset export pipeline, and initial Python data inspection are implemented.

Real grooming data is currently being collected. Because the initial real-world dataset is small, synthetic data is being used during development of the machine learning pipeline. Synthetic data is used to develop and test the ML workflow and is **not treated as evidence of real-world model performance**.

### Roadmap

* [x] Appointment persistence
* [x] Rules-based duration estimator
* [x] Actual duration recording
* [x] Completed appointment dataset export
* [x] Python dataset inspection
* [x] Initial data quality checks
* [~] Real-world data collection
* [ ] Exploratory data analysis
* [ ] Synthetic ML development dataset
* [ ] Machine learning preprocessing
* [ ] Regression model training
* [ ] Model evaluation
* [ ] Model persistence
* [ ] Python inference service
* [ ] Java/Python integration
* [ ] Production deployment

## Architecture

The project separates the application/API layer from the machine learning workflow.

```text
                         ┌─────────────────────┐
                         │      Client         │
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
                                          │ / scikit-learn │
                                          └─────────────────┘
```

The intended architecture keeps responsibilities separate:

* **Java/Spring Boot** — API, validation, business logic and persistence
* **PostgreSQL** — appointment and breed data
* **Python** — data analysis, preprocessing, model training and evaluation
* **scikit-learn** — machine learning implementation

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

### Machine Learning

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

#### Development dataset

Real-world grooming data is being collected over time. Because a meaningful real dataset is not available yet, a separate synthetic dataset is used during development to build and test the ML pipeline.

The synthetic development dataset contains 500 generated appointments and is stored at:

`data/ml/synthetic_development_appointments.csv`

The synthetic data is generated from the project's existing rules-based duration assumptions with additional random variation to simulate differences between otherwise similar grooming appointments.

**The synthetic dataset is for development and testing only. Its distribution and any machine-learning performance measured from it should not be interpreted as evidence of real-world model performance.**

The eventual model will be evaluated using real completed grooming appointments as sufficient observations become available.

#### Current ML development status

* [x] Export completed appointments from PostgreSQL
* [x] Generate ML-compatible CSV dataset
* [x] Inspect dataset with pandas
* [x] Create synthetic development dataset
* [x] Validate synthetic dataset
* [ ] Preprocess categorical and numerical features
* [ ] Train regression model
* [ ] Evaluate model using MAE, RMSE and R²
* [ ] Perform error analysis
* [ ] Evaluate using real-world grooming data
* [ ] Persist trained model
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

### Python environment

Create and activate a virtual environment:

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
```

Install the Python dependencies:

```powershell
pip install -r requirements.txt
```

### Run the application

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
* The initial duration estimator is rules-based.
* The ±15% estimate bounds have not been statistically validated.
* The machine learning model has not yet been evaluated on a sufficiently large real-world dataset.
* Groomer experience is currently recorded as an input but may have limited variation in the initial dataset.
* Model performance may change as more real grooming observations are collected.

These limitations are intentionally documented rather than presenting development results as evidence of production-level accuracy.

## Future Improvements

Potential future work includes:

* Collecting additional real-world grooming observations
* Expanding the dataset across different groomers
* Performing exploratory data analysis
* Training and comparing regression models
* Error analysis
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

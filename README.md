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

## Machine Learning

The machine learning task is a **regression problem**.

### Features

The initial model uses:

| Feature            | Type        |
| ------------------ | ----------- |
| Breed              | Categorical |
| Weight             | Numeric     |
| Coat length        | Categorical |
| Coat texture       | Categorical |
| Coat structure     | Categorical |
| Matting severity   | Categorical |
| Behaviour          | Categorical |
| Service            | Categorical |
| Groomer experience | Numeric     |

### Target

```text
actual_duration_minutes
```

The target represents the time from when the groomer begins working on the dog until grooming is complete and the dog is waiting for pickup.

### Planned evaluation

The primary evaluation metrics are:

* Mean Absolute Error (MAE)
* Root Mean Squared Error (RMSE)
* R²

The final evaluation methodology will take dataset size into account. In particular, cross-validation may be preferable while the real dataset remains relatively small.

## Dataset

Only completed appointments are exported to the initial machine learning dataset.

The ML dataset contains the nine input features and the actual duration:

```text
breed
weight_kg
coat_length
coat_texture
coat_structure
matting_severity
behaviour
service
groomer_experience_years
actual_duration_minutes
```

Customer-identifying information is deliberately excluded from the ML dataset.

Synthetic data may be used during development of the machine learning pipeline. Synthetic data will be clearly separated from real observations and will not be used to make claims about real-world predictive performance.

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

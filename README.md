# Courier Tracking System

A backend RESTful API for tracking couriers in real time and detecting their entry into stores within a configurable radius.

## Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
    - [Clone the Repository](#clone-the-repository)
    - [Build & Run](#build--run)
    - [Using Docker](#using-docker)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Contact](#contact)

## Features
- **Courier Management**: Create, update, and list active couriers.
- **Location Updates**: Couriers can push their geolocation along with a timestamp.
- **Store Entry Detection**: Automatically detects when a courier enters a store by evaluating the nearest unvisited store within a specified radius and logs the entry event.
- **Distance Metrics**: Calculate total distance traveled by each courier (Haversine).
- Geospatial support using **PostgreSQL/PostGIS** for efficient location queries.

## Technologies
- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Hibernate
- PostgreSQL with PostGIS
- Docker & Docker Compose
- Maven Wrapper
- Application Event Publisher

## Prerequisites
- Java 21 SDK
- Maven 3.6+ (included via Maven Wrapper)
- Docker & Docker Compose (optional, for containerized deployment)

## Installation

### Clone the Repository

```bash
git clone https://github.com/your-org/courier-tracking.git
cd courier-tracking/courier-tracking
```

### Build & Run
Build the project using Maven Wrapper:

```bash
./mvnw clean package -DskipTests
```
Run the application:
```bash
java -jar target/courier-tracking-0.0.1-SNAPSHOT.jar
```

The API will be available at http://localhost:8100.

### Using Docker

Build and start services with Docker Compose:

```bash
docker-compose up --build -d
```

This will start:

*  **PostgreSQL/PostGIS** on port **5432**
*   **Courier Tracking** API on port **8100**

## API Endpoints
All endpoints are prefixed with /v1 by default.

| Method | Endpoint                          | Description                           |
| ------ | --------------------------------- | ------------------------------------- |
| GET    | `/v1/courier`                     | List active couriers (with paging)    |
| POST   | `/v1/courier`                     | Create a new courier                  |
| PUT    | `/v1/courier/{id}`                | Update details of an existing courier |
| POST   | `/v1/courier/location`            | Submit a courier location update      |
| GET    | `/v1/courier/{id}/total-distance` | Retrieve total distance traveled      |

**Note:** Location updates trigger store-entry detection events behind the scenes.

**Examples**

**List Active Couriers**
```bash
curl -X GET "http://localhost:8100/api/v1/courier?page=0&size=10" \
  -H "Accept: application/json"
```
**Create a New Courier**

```bash
curl -X POST "http://localhost:8100/api/v1/courier" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sercan",
    "surname": "Çelenk",
    "phoneNumber": "12345678901",
    "identityNumber": "12345678901"
}'
```

**Update Courier Details**
```bash
curl -X PUT "http://localhost:8100/api/v1/courier/1" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sercan",
    "surname": "Çelenk",
    "phoneNumber": "12345678902",
  }'
```

**Update Courier Location**
```bash
curl -X POST "http://localhost:8100/api/v1/courier/location" \
  -H "Content-Type: application/json" \
  -d '{
    "courierId": 1,
    "lat": 40.7128,
    "lng": -74.0060,
    "time": "2025-05-25T10:15:30Z"
  }'
```

**Get Total Distance Traveled**
```bash
curl -X GET "http://localhost:8100/v1/courier/1/total-distance" \
  -H "Accept: application/json"
```


## Configuration

The application can be configured via environment variables or application.properties:

| Property                     | Description                              | Default |
| ---------------------------- | ---------------------------------------- |---------|
| `spring.datasource.url`      | JDBC URL of the PostgreSQL database      | —       |
| `spring.datasource.username` | Database username                        | —       |
| `spring.datasource.password` | Database password                        | —       |
| `store.entry.radius`         | Radius (in meters) to detect store entry | 100     |

## Project Structure
```
├── src/main/java/com/migros/couriertracking
│   ├── common            # Common configurations & exceptions
│   ├── courier           # Courier entity, DTOs, service & controller
│   ├── store             # Store & entry detection logic
│   └── ...
├── src/main/resources    # Application configuration files
├── Dockerfile            # Docker image definition
├── docker-compose.yml    # Docker Compose setup
├── pom.xml               # Maven project file
└── mvnw*, mvnw.cmd       # Maven Wrapper scripts
```

## Contact

Maintainer: Sercan Çelenk

GitHub: sercancelenk81

Email: sercancelenk81@gmail.com





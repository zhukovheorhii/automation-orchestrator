# Automation Secret

Service responsible for sharing secrets across instances of automation test runners.

## Overview

This service provides centralized management of test configuration settings and test user credentials for automated testing. It manages test user locking/unlocking mechanisms and configuration settings that are shared across multiple test runner instances.

## Technology Stack

- **Java**: 25+
- **Spring Boot**: 4.x
- **Gradle**: Kotlin DSL
- **Database**: PostgreSQL
- **Migration Tool**: Flyway
- **Containerization**: Jib plugin

## Architecture

This project follows Clean Architecture principles with a multi-module Gradle structure:

```
├── domain
├── infrastructure
│   ├── rest-api
│   │   ├── open-api
│   ├── jdbc-storage-connector
│   ├── application
```

### Module Descriptions

#### Domain Module
The `domain` module contains pure business logic, entities, and port interfaces. It has no dependencies on infrastructure or framework-specific code, except for basic Spring Context annotations for IoC. This module must be fully covered with unit tests.

**Key components:**
- Domain models and entities
- Business services
- Port interfaces for external adapters
- Domain exceptions

#### Infrastructure Modules

**rest-api**: Contains OpenAPI contracts and generated server-side code (DTOs, Controller interfaces) using the openapi-generator.

**jdbc-storage-connector**: Implements database access using Spring Data JDBC and PostgreSQL. Database schema is managed via Flyway migrations located in `src/main/resources/db/migrations/`.

**application**: The composition root that wires all modules together. Contains the main `@SpringBootApplication` class with `@EnableScheduling` support.

## API Endpoints

The service exposes the following REST endpoints:

- **GET /api/settings**: Retrieve configuration settings for automated tests
- **GET /api/test-user**: Lock and retrieve a test user for exclusive use

Full API specification available in `infrastructure/rest-api/open-api/src/main/resources/openapi.yaml`

## Local Development

### Prerequisites

- Java 25
- Docker (for PostgreSQL)

### Running Locally

1. Start the PostgreSQL database:
```bash
docker-compose up -d
```

2. Run the application:
```bash
./gradlew :infrastructure:application:bootRun
```

The service will be available at:
- Application: http://localhost:8080
- Management endpoints: http://localhost:9000

### Testing

Run all tests:
```bash
./gradlew test
```

Generate coverage report:
```bash
./gradlew testCodeCoverageReport
```

## Configuration

The service uses Spring Boot configuration with profiles. Default configuration is in `infrastructure/application/src/main/resources/application.yml`.

Key configuration properties:
- **Database**: PostgreSQL connection (default: localhost:5432/secret_db)
- **Scheduler**: Test user cleanup TTL (default: 20 minutes)
- **Logging**: Configured via commons-logging library

## Building

Build the project:
```bash
./gradlew build
```

Build Docker image:
```bash
./gradlew jibDockerBuild
```

## Architectural Constraints

- **No JPA/Hibernate**: Use Spring Data JDBC only
- **Database migrations**: All schema changes via Flyway scripts
- **Contract-first**: REST API follows OpenAPI specification
- **Dependency direction**: Infrastructure → Domain (infrastructure modules depend on domain, domain has no outward dependencies)

# Test Keycloak Integration

A learning project for experimenting with Keycloak integration in a Spring Boot REST API.

## Overview

The purpose of this project is to explore and test authentication and authorization using Keycloak and Spring Security.

The project will evolve incrementally as different integration scenarios are implemented and verified.

## Technology Stack

- Java 21
- Spring Boot 4.1.1
- Spring MVC
- Maven
- Keycloak (planned)
- Spring Security (planned)

## Prerequisites

Make sure the following tools are installed:

- JDK 21
- Apache Maven

Verify your installation:

```bash
java -version
mvn -version
```

## Build and Run

Build the project:

```bash
mvn clean package
```

Run the application:

```bash
mvn spring-boot:run
```

By default, the application uses port `8080`.

## Configuration

Application configuration is located in:

```text
src/main/resources/application.yaml
```

Integration-specific settings will be added as the Keycloak integration is implemented.

Credentials, client secrets, access tokens, and other sensitive information must not be committed to the repository.

## Planned Work

The project may cover the following topics:

- Running and configuring a Keycloak instance.
- Creating a realm and configuring clients.
- Integrating Spring Security with Keycloak.
- Validating JWT access tokens in a REST API.
- Configuring roles and authorization rules.
- Testing authorized and unauthorized requests.
- Handling authentication failures and insufficient permissions.
- Exploring OpenID Connect (OIDC) where applicable.

The exact implementation will depend on the integration scenarios being tested.

## Project Status

The project currently contains the minimal Spring Boot application setup. Keycloak integration is planned and will be implemented incrementally.

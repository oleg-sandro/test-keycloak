# Test Keycloak Integration

A learning project for experimenting with Keycloak, Spring Security, and JWT-based authentication and authorization in a Spring Boot REST API.

## Overview

The project demonstrates how to secure REST endpoints using access tokens issued by Keycloak, authenticate API requests, and enforce role-based access control.

It also explores machine-to-machine authentication using the OAuth 2.0 Client Credentials grant.

This is a learning project intended for local development and experimentation, not a production-ready application.

## Technology Stack

- Java 21
- Spring Boot 4.1.1
- Spring MVC
- Spring Security OAuth2 Resource Server
- Keycloak 26.8.1
- OAuth 2.0 and OpenID Connect (OIDC)
- JWT (JSON Web Tokens)
- Maven
- Docker Compose

## Prerequisites

Install the following tools:

- JDK 21
- Apache Maven
- Docker Desktop with Docker Compose

Verify the installations:

```powershell
java -version
mvn -version
docker --version
docker compose version
```

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/oleg-sandro/test-keycloak.git
cd test-keycloak
```

### 2. Configure Keycloak

Create a `.env` file in the project root:

```dotenv
KEYCLOAK_USERNAME=admin
KEYCLOAK_PASSWORD=change-me-local-only
```

These credentials are for local development only. Do not commit `.env` to Git.

Start Keycloak:

```powershell
docker compose up -d
```

Check the container status:

```powershell
docker compose ps
```

Follow the Keycloak logs:

```powershell
docker compose logs -f keycloak
```

Open the Keycloak Admin Console:

http://localhost:8081/admin/

Sign in using the credentials defined in `.env`.

Keycloak is available at `http://localhost:8081`. Docker Compose binds the port to `127.0.0.1` and persists Keycloak data in the `keycloak_data` volume.

### 3. Configure the realm and client

In Keycloak, configure the following resources:

1. Create the `test-realm` realm.
2. Create the `test-client` OpenID Connect client for interactive authentication testing, if needed.
3. Create the `test-api-client` client for machine-to-machine authentication.
4. Enable client authentication and service accounts for `test-api-client`.
5. Create the realm roles `api-user` and `api-admin`.
6. Assign the required roles to the appropriate users or service accounts.

For the Client Credentials flow, obtain the client secret from the `test-api-client` credentials page.

The application is configured to trust tokens issued by:

```text
http://localhost:8081/realms/test-realm
```

See `src/main/resources/application.yaml`.

### 4. Build and run the Spring Boot application

Build the application:

```powershell
mvn clean package
```

Run it:

```powershell
mvn spring-boot:run
```

The API is available at `http://localhost:8080`.

## REST Endpoints

All endpoints use the `/api/v1/test` base path.

| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/v1/test/auth/public` | Public |
| GET | `/api/v1/test/auth/private` | Authenticated requests |
| GET | `/api/v1/test/auth/restricted` | Denied |
| GET | `/api/v1/test/role/user` | Requires `api-user` |
| GET | `/api/v1/test/role/admin` | Requires `api-admin` |

The authorization rules are defined in `SecurityConfig`.

The public endpoint does not require an access token. The private endpoint requires a valid JWT. Role-protected endpoints require the corresponding realm role. Requests to endpoints not explicitly permitted by the security configuration are denied.

## Obtaining an Access Token

The project supports testing with access tokens issued by Keycloak.

For machine-to-machine authentication, use the Client Credentials grant.

In PowerShell, set the client credentials:

```powershell
$clientId = "test-api-client"
$clientSecret = "YOUR_CLIENT_SECRET"
```

Request an access token:

```powershell
$response = Invoke-RestMethod `
    -Method Post `
    -Uri "http://localhost:8081/realms/test-realm/protocol/openid-connect/token" `
    -ContentType "application/x-www-form-urlencoded" `
    -Body @{
        grant_type = "client_credentials"
        client_id = $clientId
        client_secret = $clientSecret
    }

$token = $response.access_token
```

Check the token response without printing the token itself:

```powershell
$response | Select-Object token_type, expires_in, scope
```

The token can now be used to call protected API endpoints.

## Testing the API

### Public endpoint

```powershell
curl.exe -i http://localhost:8080/api/v1/test/auth/public
```

Expected response: `200 OK`.

### Private endpoint without a token

```powershell
curl.exe -i http://localhost:8080/api/v1/test/auth/private
```

Expected response: `401 Unauthorized`.

### Private endpoint with a valid token

```powershell
curl.exe -i `
    -H "Authorization: Bearer $token" `
    http://localhost:8080/api/v1/test/auth/private
```

Expected response: `200 OK`.

### Role-protected endpoint

For the user role:

```powershell
curl.exe -i `
    -H "Authorization: Bearer $token" `
    http://localhost:8080/api/v1/test/role/user
```

For the admin role:

```powershell
curl.exe -i `
    -H "Authorization: Bearer $token" `
    http://localhost:8080/api/v1/test/role/admin
```

A valid token without the required role should result in `403 Forbidden`. A token containing the required role should result in `200 OK`, once realm-role conversion is correctly wired into the resource server configuration.

## Security Implementation

The application uses Spring Security OAuth2 Resource Server to validate JWT access tokens issued by Keycloak.

The security configuration includes:

- JWT-based authentication.
- An issuer URI configured through `spring.security.oauth2.resourceserver.jwt.issuer-uri`.
- Public and authenticated endpoint rules.
- Role-based authorization using `api-user` and `api-admin`.
- A default-deny policy for unmatched requests.
- A custom `JwtAuthenticationConverter` intended to map Keycloak realm roles from `realm_access.roles` to Spring Security authorities with the `ROLE_` prefix.

## Project Structure

```text
src/
└── main/
    ├── java/com/test/keycloak/
    │   ├── config/
    │   │   └── SecurityConfig.java
    │   └── controller/
    │       ├── AuthTestController.java
    │       └── RoleTestController.java
    └── resources/
        └── application.yaml

compose.yaml
pom.xml
```

## Security Notes

- Do not commit `.env`, client secrets, passwords, or access tokens.
- Use dedicated clients for different applications and integrations.
- Grant only the roles and permissions required by each client.
- Use HTTPS and appropriate token validation, including issuer and audience validation, in production.
- Store secrets in a dedicated secret-management system in production.
- The local Keycloak configuration uses `start-dev` and is intended only for development.

## Future Improvements

- Add automated unit and integration tests.
- Add tests for authentication failures and insufficient permissions.
- Configure and test audience validation.
- Externalize environment-specific Keycloak settings.
- Explore additional OAuth 2.0 and OIDC integration scenarios.

## License

No license has been specified yet.

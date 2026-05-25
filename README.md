# JWT Token Doctor
A Spring Boot application for JWT token validation and analysis.

## Features
- REST API for JWT token validation
- Claims extraction and display
- Comprehensive error handling
- Easy token verification

## Project Structure
```
jwt-token-doctor/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/jwtdoctor/
│   │   │       ├── JwtTokenDoctorApplication.java    (Main entry point)
│   │   │       ├── controller/
│   │   │       │   └── JwtController.java             (REST endpoints)
│   │   │       ├── service/
│   │   │       │   └── JwtValidationService.java      (Validation logic)
│   │   │       └── dto/
│   │   │           ├── JwtValidationRequest.java
│   │   │           └── JwtValidationResponse.java
│   │   └── resources/
│   │       └── application.properties                 (Configuration)
│   └── test/
│       └── java/
│           └── com/jwtdoctor/
│               └── controller/
│                   └── JwtControllerTest.java
├── pom.xml                                            (Maven configuration)
└── BUILD.md                                           (Build instructions)
```

## Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build
```bash
mvn clean package
```

### Run
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Health Check
```
GET /api/jwt/health
```

### Validate JWT Token
```
POST /api/jwt/validate
Content-Type: application/json

{
  "token": "your-jwt-token-here"
}
```

#### Response Example
```json
{
  "valid": true,
  "message": "Token is valid",
  "claims": {
    "sub": "1234567890",
    "name": "John Doe",
    "iat": 1516239022
  }
}
```

## Configuration

Edit `src/main/resources/application.properties` to customize:
- Server port (default: 8080)
- JWT secret key for validation

## Technologies Used
- Spring Boot 3.2.0
- JJWT (JSON Web Token Library)
- Lombok
- Maven
- JUnit 5 for testing

## License
See LICENSE file for details

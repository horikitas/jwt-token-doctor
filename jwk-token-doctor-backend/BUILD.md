# Build Instructions

## Prerequisites
- Java 17 or higher
- Maven 3.6+

## Building the Project

```bash
# Build the project
mvn clean package

# Run tests
mvn test
```

## Running the Application

```bash
# Run using Maven
mvn spring-boot:run

# Or run the JAR directly
java -jar target/jwt-token-doctor-1.0.0.jar
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

### Response Example
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

Edit `src/main/resources/application.properties` to modify:
- Server port (default: 8080)
- JWT secret key


# JWT Token Doctor

JWT Token Doctor is a full-stack tool for decoding JWTs and diagnosing common token security issues. The backend exposes a Spring Boot API that parses JWTs and applies diagnosis rules, while the frontend provides a Next.js UI for pasting a token and viewing the results.

## Features

- Decode JWT header and payload without requiring a signing secret
- Detect common issues such as `alg=none`, missing `exp`, expired tokens, invalid `exp`, and long-lived tokens
- Return findings with severity, code, message, and recommendation
- Calculate an overall risk score and severity
- Browser UI for diagnosing tokens locally
- Cucumber and Spring Boot test coverage for backend behavior

## Project Structure

```text
jwt-token-doctor/
|-- README.md
|-- LICENSE
|-- jwt-doctor-backend/
|   |-- BUILD.md
|   |-- pom.xml
|   |-- src/
|   |   |-- main/
|   |   |   |-- java/org/horikitas/tokendoctor/
|   |   |   |   |-- JwtDoctorApplication.java
|   |   |   |   |-- Utils.java
|   |   |   |   |-- config/
|   |   |   |   |   `-- AppConfig.java
|   |   |   |   |-- controller/
|   |   |   |   |   `-- JwtController.java
|   |   |   |   |-- model/
|   |   |   |   |   |-- DecodedJwt.java
|   |   |   |   |   |-- Finding.java
|   |   |   |   |   |-- Severity.java
|   |   |   |   |   |-- TokenDiagnosis.java
|   |   |   |   |   |-- TokenDiagnosisRequest.java
|   |   |   |   |   `-- TokenDiagnosisResponse.java
|   |   |   |   `-- service/
|   |   |   |       |-- JwtDiagnosisService.java
|   |   |   |       `-- rules/
|   |   |   |           |-- AlgNoneRule.java
|   |   |   |           |-- JwtRule.java
|   |   |   |           |-- LongLivedTokenRule.java
|   |   |   |           |-- MissingExpRule.java
|   |   |   |           `-- TokenExpiredRule.java
|   |   |   `-- resources/
|   |   |       `-- application.yaml
|   |   `-- test/
|   |       |-- java/org/horikitas/tokendoctor/
|   |       |   |-- JwtDoctorApplicationTests.java
|   |       |   `-- cucumber/
|   |       |       |-- CucumberSpringConfiguration.java
|   |       |       |-- CucumberTest.java
|   |       |       `-- JwtDiagnosisSteps.java
|   |       `-- resources/features/
|   |           `-- jwt_diagnosis.feature
`-- jwt-doctor-frontend/
    |-- package.json
    |-- next.config.ts
    |-- tsconfig.json
    |-- app/
    |   |-- globals.css
    |   |-- layout.tsx
    |   `-- page.tsx
    |-- public/
    `-- src/lib/
        `-- diagnoseToken.ts
```

## Prerequisites

- Java 25
- Maven 3.9+
- Node.js 20+
- npm

## Run Locally

Start the backend:

```bash
cd jwt-doctor-backend
mvn spring-boot:run
```

The backend starts on `http://localhost:8080`.

Start the frontend in a second terminal:

```bash
cd jwt-doctor-frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:3000`.

If the frontend cannot reach the API, set the API base URL before starting Next.js:

```bash
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080 npm run dev
```

On Windows PowerShell:

```powershell
$env:NEXT_PUBLIC_API_BASE_URL = "http://localhost:8080"
npm run dev
```

## Backend API

### Diagnose a JWT

```http
POST /api/v1/tokens/diagnose
Content-Type: application/json
```

Request body:

```json
{
  "token": "your-jwt-token-here"
}
```

Example response:

```json
{
  "validFormat": true,
  "header": {
    "alg": "HS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "1234567890",
    "name": "John Doe",
    "iat": 1516239022,
    "exp": 1516242622
  },
  "findings": [],
  "riskScore": 0,
  "severity": "INFO"
}
```

## Test and Build

Backend:

```bash
cd jwt-doctor-backend
mvn test
mvn clean package
```

Frontend:

```bash
cd jwt-doctor-frontend
npm run lint
npm run build
```

## Configuration

Backend configuration lives in `jwt-doctor-backend/src/main/resources/application.yaml`.

The backend CORS configuration currently allows the frontend origin `http://localhost:3000`.

The frontend reads the API base URL from `NEXT_PUBLIC_API_BASE_URL`.

## Technologies

- Spring Boot 4
- Java 25
- Maven
- Cucumber
- JUnit Platform
- Next.js 16
- React 19
- TypeScript
- Tailwind CSS

## License

See `LICENSE` for details.

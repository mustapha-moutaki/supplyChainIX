***

# Supply Chain Authentication Module

This project is a Spring Boot application implementing a secure **JWT (JSON Web Token)** authentication system. It includes features for user registration, login, and token management using **Access Tokens** (short-lived) and **Refresh Tokens** (long-lived).

## 🛠 Tech Stack
*   **Java 17**
*   **Spring Boot 3**
*   **Spring Security**
*   **Spring Data JPA**
*   **PostgreSQL**
*   **Lombok**
*   **JWT (io.jsonwebtoken)**

---

## ⚙️ Configuration

Before running the application, ensure your `src/main/resources/application.yml` is configured correctly:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/supplychainx_db
    username: your_db_user
    password: your_db_password
  jpa:
    hibernate:
      ddl-auto: update

# JWT Configuration
application:
  jwt:
    secret: YOUR_256_BIT_SECRET_KEY_HERE
    access-token-expiration: 900000      # 15 minutes
    refresh-token-expiration: 604800000  # 7 days
```

---

## 🚀 API Endpoints

Base URL: `http://localhost:8080`

### 1. Register User
Create a new user account.

*   **URL:** `/api/auth/register`
*   **Method:** `POST`
*   **Auth Required:** No

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@mail.com",
  "password": "password123",
  "role": "ADMIN"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "b6dccfd1-927e-4390-bfe0-a55992cdaf45"
}
```

---

### 2. Authenticate (Login)
Login to receive new tokens. This handles **User/Refresh Token rotation** (updates the refresh token in the DB).

*   **URL:** `/api/auth/authenticate`
*   **Method:** `POST`
*   **Auth Required:** No

**Request Body:**
```json
{
  "email": "john@mail.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "b6dccfd1-927e-4390-bfe0-a55992cdaf45"
}
```

---

### 3. Refresh Token
Generate a new **Access Token** when the old one expires, using the **Refresh Token**.

*   **URL:** `/api/auth/refresh-token`
*   **Method:** `POST`
*   **Auth Required:** No

**Request Body:**
```json
{
  "token": "b6dccfd1-927e-4390-bfe0-a55992cdaf45"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "b6dccfd1-927e-4390-bfe0-a55992cdaf45"
}
```
*Note: If the refresh token is expired, the server will return `403 Forbidden`.*

---

### 4. Accessing Secured Endpoints
To access any other endpoint in the system, you must provide the **Access Token** in the header.

*   **Header Name:** `Authorization`
*   **Value:** `Bearer <YOUR_ACCESS_TOKEN>`

**Example:**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2hu...
```

---

## 🏃‍♂️ How to Run

1.  **Clone the repository.**
2.  **Configure Database:** Create a PostgreSQL database named `supplychainx_db`.
3.  **Run with Maven:**
    ```bash
    mvn spring-boot:run
    ```
    *Or with Docker if configured:*
    ```bash
    docker-compose up --build
    ```
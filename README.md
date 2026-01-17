# Reservation System

A comprehensive Spring Boot application for managing room reservations, equipment, and user authentication with
role-based access control.

## Features

- **User Authentication**: Secure registration, login, and logout using JWT and Refresh Tokens.
- **Role-Based Access Control**:
    - `ADMIN`: Manage rooms and equipment.
    - `MANAGER`: Manage all reservations.
    - `USER`: Manage own reservations.
    - `VIEWER`: View room availability.
- **Room Management**: Create, update, and delete rooms with capacity specifications.
- **Equipment Management**: Track and assign equipment to specific rooms.
- **Reservation System**: Book rooms for specific time slots, avoiding overlaps.
- **API Documentation**: Integrated Swagger/OpenAPI for easy endpoint exploration.

## Tech Stack

- **Backend**: Java 25, Spring Boot 4.0.1
- **Security**: Spring Security, OAuth2 Resource Server, JWT
- **Database**: MySQL 8+
- **Database Migration**: Flyway
- **Mapping**: MapStruct
- **Utilities**: Lombok, spring-dotenv

## Prerequisites

- **Java 25** or higher
- **Maven 3.9+**
- **MySQL 8.0+**

## Setup & Installation

### 1. Database Configuration

Create a MySQL database named `reservation-system`.

The application uses `spring-dotenv` to manage sensitive information. Create a `.env` file in the project root or
configure the following environment variables:

```properties
DB_URL=jdbc:mysql://localhost:3306/reservation-system
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_SECRET=your_very_long_and_secure_jwt_secret_key
ALLOWED_ORIGINS=http://localhost:3000
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The database schema will be automatically created and updated by Flyway on startup.

## 📖 API Documentation

Once the application is running, you can access the Swagger UI documentation at:

`http://localhost:8080/swagger-ui.html`

## 🔐 Key Endpoints

- `POST /auth/register`: Register a new user.
- `POST /auth/login`: Authenticate and receive an access token.
- `GET /rooms`: List all available rooms.
- `GET /reservations/user`: View your own reservations.
- `POST /reservations/user`: Book a room.
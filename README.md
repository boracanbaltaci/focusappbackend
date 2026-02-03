# Focus App Backend

A professional Spring Boot backend application for a focus time tracking application with subscription management system. Built with Java 17, MongoDB, and includes Swagger API documentation.

## Features

- ✅ **User Authentication & Authorization** - JWT-based authentication with Spring Security
- ✅ **Focus Session Management** - Create, update, complete, and track focus sessions
- ✅ **Subscription System** - Multiple subscription plans (FREE, BASIC, PREMIUM, ENTERPRISE)
- ✅ **MongoDB Integration** - Flexible NoSQL database with Spring Data MongoDB
- ✅ **API Documentation** - Interactive Swagger UI for API testing
- ✅ **Security** - BCrypt password encoding, JWT token validation
- ✅ **Exception Handling** - Global exception handler for consistent error responses
- ✅ **Validation** - Request validation using Jakarta Validation

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.2
- **Database**: MongoDB
- **Security**: Spring Security + JWT (jjwt 0.12.5)
- **API Documentation**: Springdoc OpenAPI 2.3.0
- **Build Tool**: Maven
- **Other**: Lombok, Spring Data MongoDB

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MongoDB instance (local or cloud)

## Configuration

The application uses environment variables for sensitive configuration. You only need to provide:

### Required Environment Variables

```bash
MONGODB_URI=mongodb://localhost:27017
MONGODB_DATABASE=focusapp
```

### Optional Environment Variables

```bash
SERVER_PORT=8080                    # Default: 8080
JWT_SECRET=your-secret-key          # Default provided (change in production!)
JWT_EXPIRATION=86400000             # Default: 24 hours in milliseconds
```

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/boracanbaltaci/focusappbackend.git
cd focusappbackend
```

### 2. Set up MongoDB

Option A - Local MongoDB:
```bash
# Install and start MongoDB locally
mongod
```

Option B - MongoDB Atlas (Cloud):
```bash
# Sign up at https://www.mongodb.com/cloud/atlas
# Create a cluster and get your connection string
export MONGODB_URI="mongodb+srv://username:password@cluster.mongodb.net"
export MONGODB_DATABASE="focusapp"
```

### 3. Build the application

```bash
mvn clean install
```

### 4. Run the application

```bash
# Using Maven
mvn spring-boot:run

# Or using java -jar
java -jar target/focus-backend-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

API documentation JSON:
```
http://localhost:8080/api-docs
```

## API Endpoints

### Authentication APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/signup` | Register new user | No |
| POST | `/api/auth/login` | Login and get JWT token | No |

### Focus Session APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/sessions` | Create new focus session | Yes |
| GET | `/api/sessions` | Get all user's sessions | Yes |
| GET | `/api/sessions/{id}` | Get specific session | Yes |
| GET | `/api/sessions/range` | Get sessions in date range | Yes |
| PUT | `/api/sessions/{id}` | Update session | Yes |
| POST | `/api/sessions/{id}/complete` | Complete session | Yes |
| DELETE | `/api/sessions/{id}` | Delete session | Yes |

### Subscription APIs

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/subscriptions` | Get user's subscription | Yes |
| POST | `/api/subscriptions` | Create/upgrade subscription | Yes |
| POST | `/api/subscriptions/cancel` | Cancel subscription | Yes |

## Usage Examples

### 1. Register a new user

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "password123"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": "507f1f77bcf86cd799439011",
  "username": "johndoe",
  "email": "john@example.com",
  "roles": ["ROLE_USER"]
}
```

### 3. Create a focus session

```bash
curl -X POST http://localhost:8080/api/sessions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Deep Work Session",
    "description": "Working on backend development",
    "category": "Development",
    "tags": "coding,backend"
  }'
```

### 4. Upgrade subscription

```bash
curl -X POST http://localhost:8080/api/subscriptions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "plan": "PREMIUM",
    "autoRenew": true,
    "paymentMethod": "credit_card"
  }'
```

## Data Models

### User
- Username (unique)
- Email (unique)
- Password (encrypted)
- First Name, Last Name
- Roles
- Active status
- Timestamps

### Focus Session
- Title, Description
- Start/End time
- Duration (in seconds)
- Category, Tags
- Completion status
- User reference
- Timestamps

### Subscription
- User reference
- Plan (FREE, BASIC, PREMIUM, ENTERPRISE)
- Status (ACTIVE, INACTIVE, CANCELLED, EXPIRED, TRIAL)
- Start/End date
- Auto-renew setting
- Payment method
- Timestamps

## Security

- **Password Encryption**: BCrypt with strength 10
- **JWT Authentication**: HS256 algorithm with configurable secret and expiration
- **CORS**: Configured for security
- **CSRF**: Disabled (suitable for stateless REST APIs)
- **Authorization**: Role-based access control

## Development

### Project Structure

```
src/main/java/com/focusapp/backend/
├── config/              # Configuration classes
│   ├── SecurityConfig.java
│   └── OpenApiConfig.java
├── controller/          # REST Controllers
│   ├── AuthController.java
│   ├── FocusSessionController.java
│   └── SubscriptionController.java
├── dto/                 # Data Transfer Objects
├── exception/           # Exception handling
├── model/              # Domain models
│   ├── User.java
│   ├── FocusSession.java
│   └── Subscription.java
├── repository/         # MongoDB repositories
├── security/           # Security components
│   ├── JwtUtils.java
│   ├── AuthTokenFilter.java
│   └── UserDetailsImpl.java
└── service/            # Business logic
```

### Building for Production

```bash
# Build with production profile
mvn clean package -DskipTests

# Run with production settings
export JWT_SECRET="your-very-long-secret-key-at-least-256-bits"
export MONGODB_URI="your-production-mongodb-uri"
export MONGODB_DATABASE="focusapp_prod"
java -jar target/focus-backend-1.0.0.jar
```

## Troubleshooting

### MongoDB Connection Issues

```bash
# Check MongoDB is running
mongosh

# Verify connection string
echo $MONGODB_URI
```

### Port Already in Use

```bash
# Change the port
export SERVER_PORT=8081
mvn spring-boot:run
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License 2.0

## Support

For support, email support@focusapp.com or open an issue in the repository.
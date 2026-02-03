# JWT Configuration Error Fix Guide

## Problem

Application fails to start with errors like:

```
Error creating bean with name 'authTokenFilter': 
Unsatisfied dependency expressed through constructor parameter 0: 
Error creating bean with name 'jwtUtils': 
Injection of autowired dependencies failed
```

or

```
Could not resolve placeholder 'JWT_SECRET' in value "${JWT_SECRET}"
```

## Root Cause

The `JWT_SECRET` environment variable is required but not set, causing Spring Boot to fail when injecting the configuration value into the `JwtUtils` bean.

## Solution

The application now includes a default JWT secret for **development purposes only**. The application will start without requiring the `JWT_SECRET` environment variable.

### For Development

Simply run the application - it will use the default development secret:

```bash
mvn spring-boot:run
```

or in IntelliJ: Click the Run button ▶️

### For Production (IMPORTANT!)

**⚠️ SECURITY WARNING**: The default secret is NOT secure for production use!

Always set a secure JWT secret in production:

#### Option 1: Environment Variable

```bash
export JWT_SECRET="your-very-secure-random-secret-minimum-256-bits-long"
java -jar target/focus-backend-1.0.0.jar
```

#### Option 2: Application Properties

In `application-prod.yml`:

```yaml
jwt:
  secret: ${JWT_SECRET}  # This will require the env var in production
```

Then run with production profile:

```bash
java -jar -Dspring.profiles.active=prod target/focus-backend-1.0.0.jar
```

#### Option 3: Docker/Kubernetes

Set environment variable in your deployment:

**Docker Compose:**
```yaml
services:
  backend:
    environment:
      - JWT_SECRET=your-secure-secret-here
```

**Kubernetes:**
```yaml
env:
  - name: JWT_SECRET
    valueFrom:
      secretKeyRef:
        name: app-secrets
        key: jwt-secret
```

## Generating a Secure Secret

Use one of these methods to generate a secure random secret:

### Using OpenSSL
```bash
openssl rand -base64 32
```

### Using Python
```bash
python -c "import secrets; print(secrets.token_urlsafe(32))"
```

### Using Node.js
```bash
node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"
```

## Security Best Practices

1. **Never commit secrets to version control**
   - The `.env` file is in `.gitignore` for this reason
   - Use `.env.example` as a template only

2. **Use different secrets for different environments**
   - Development, staging, and production should have unique secrets

3. **Rotate secrets periodically**
   - Change JWT secrets regularly in production
   - Have a process to invalidate old tokens

4. **Store secrets securely**
   - Use environment variables or secret management systems
   - Never hardcode secrets in application code

5. **Minimum secret length**
   - For HS256 algorithm (used by this app), minimum 256 bits (32 bytes)
   - Longer is better - 64+ bytes recommended

## Verification

After setting up JWT configuration, verify the application starts:

```bash
# Start the application
mvn spring-boot:run
```

You should see:
```
Started FocusAppBackendApplication in X.XXX seconds
Tomcat started on port(s): 8080 (http)
```

## Configuration Details

The JWT configuration in `application.yml`:

```yaml
jwt:
  # Development default (will be used if JWT_SECRET not set)
  secret: ${JWT_SECRET:dev-secret-key-CHANGE-THIS-IN-PRODUCTION...}
  expiration: ${JWT_EXPIRATION:86400000} # 24 hours
```

The syntax `${JWT_SECRET:default-value}` means:
- Use `JWT_SECRET` environment variable if available
- Otherwise, use the default value
- The default is only suitable for development

## Related Documentation

- [IntelliJ Setup Guide](INTELLIJ_SETUP.md)
- [Turkish Quick Start](TURKISH_QUICKSTART.md)
- [Main README](../README.md)
- [Environment Variables](.env.example)

# IntelliJ IDEA Setup Guide

## Opening the Project

1. **Open IntelliJ IDEA**
2. **Open the project**:
   - File → Open
   - Select the `focusappbackend` folder
   - Click OK

## First-Time Setup

### 1. Import Maven Project
When you first open the project, IntelliJ should automatically detect the Maven project and show a popup:
- Click "Import Maven Projects" or
- Enable Auto-Import in the Maven popup

If not, manually trigger Maven import:
- Right-click on `pom.xml` → Maven → Reload Project
- Or use: View → Tool Windows → Maven → Reload All Maven Projects (⌘⇧I on Mac, Ctrl+Shift+O on Windows)

### 2. Configure JDK
The project requires Java 17:

1. Go to: File → Project Structure (⌘; on Mac, Ctrl+Alt+Shift+S on Windows)
2. Under Project Settings → Project:
   - Set SDK to Java 17
   - Set Language Level to 17
3. Click Apply

If Java 17 is not available:
- Click "Add SDK" → "Download JDK"
- Select version 17 (recommend Azul Zulu, Amazon Corretto, or Eclipse Temurin)
- Click Download

### 3. Enable Annotation Processing
For Lombok support:

1. Go to: Settings/Preferences → Build, Execution, Deployment → Compiler → Annotation Processors
2. Check "Enable annotation processing"
3. Click Apply

### 4. Install Lombok Plugin (if needed)
1. Go to: Settings/Preferences → Plugins
2. Search for "Lombok"
3. Install the Lombok plugin
4. Restart IntelliJ

## Running the Application

### Method 1: Using the Run Configuration (Recommended)
The project includes a pre-configured Spring Boot run configuration:

1. Look for the dropdown in the top-right toolbar
2. Select "FocusAppBackendApplication"
3. Click the green Run button (▶) or press Shift+F10 (Windows) / ⌃R (Mac)

### Method 2: Run from Main Class
1. Open `src/main/java/com/focusapp/backend/FocusAppBackendApplication.java`
2. Right-click anywhere in the file
3. Select "Run 'FocusAppBackendApplication.main()'"

### Method 3: Using Maven
1. Open the Maven tool window (View → Tool Windows → Maven)
2. Expand Plugins → spring-boot
3. Double-click `spring-boot:run`

## Environment Setup

Before running, set up environment variables:

### Option 1: Edit Run Configuration
1. Click on the run configuration dropdown → Edit Configurations
2. Select "FocusAppBackendApplication"
3. In "Environment variables", add:
   ```
   JWT_SECRET=your-secret-key-at-least-256-bits-long-for-production-use-only
   MONGODB_URI=mongodb://localhost:27017
   MONGODB_DATABASE=focusapp
   ```
4. Click Apply

### Option 2: Use .env file (with EnvFile plugin)
1. Install "EnvFile" plugin from marketplace
2. Create `.env` file from `.env.example`:
   ```bash
   cp .env.example .env
   ```
3. Edit run configuration and enable EnvFile support

### Option 3: Application Properties
Create `src/main/resources/application-local.yml`:
```yaml
jwt:
  secret: your-development-secret-key-change-in-production

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017
      database: focusapp
```
Then run with: `-Dspring.profiles.active=local`

## Starting MongoDB

### Using Docker Compose (Recommended)
```bash
docker-compose up -d
```

### Or install MongoDB locally
- macOS: `brew install mongodb-community`
- Windows: Download from mongodb.com
- Linux: Use your package manager

## Troubleshooting

### Run button is disabled
1. **Maven not imported**: 
   - Right-click `pom.xml` → Maven → Reload Project
   
2. **JDK not configured**:
   - File → Project Structure → Project → Set SDK to Java 17
   
3. **No run configuration**:
   - Click the dropdown → "Edit Configurations" → "+" → "Spring Boot"
   - Main class: `com.focusapp.backend.FocusAppBackendApplication`
   - Module: `focus-backend`

### Build fails
1. **Clean and rebuild**:
   - Build → Clean Project
   - Build → Rebuild Project
   
2. **Invalidate caches**:
   - File → Invalidate Caches / Restart

### ClassNotFoundException: com.focusapp.backend.FocusAppBackendApplication
This error means the project hasn't been compiled yet. Solutions:

1. **Compile with Maven** (Recommended):
   ```bash
   mvn clean compile
   ```
   
2. **Build in IntelliJ**:
   - Build → Build Project (Ctrl+F9 / ⌘F9)
   - Or: Build → Rebuild Project
   
3. **Check Maven import**:
   - Right-click `pom.xml` → Maven → Reload Project
   - View → Tool Windows → Maven → Reload All Maven Projects
   
4. **Verify run configuration**:
   - The run configuration should automatically compile before running
   - If not, edit the configuration and ensure "Maven compile" is in "Before launch" tasks

The compiled classes should be in `target/classes/` directory. If this directory is missing, the project needs to be built.

### Lombok not working
1. **Enable annotation processing**: Settings → Compiler → Annotation Processors
2. **Install plugin**: Settings → Plugins → Search "Lombok"
3. **Rebuild**: Build → Rebuild Project

### MongoDB connection error
1. **Start MongoDB**: `docker-compose up -d`
2. **Check connection**: Try connecting with MongoDB Compass or mongosh
3. **Verify environment variables** in run configuration

### JWT Configuration Error
**Error**: `Error creating bean with name 'jwtUtils'` or `Unsatisfied dependency`

This error occurred when `JWT_SECRET` was not set. **This has been fixed!**

The application now includes a default JWT secret for development. However:

⚠️ **For Production**: Always set a secure `JWT_SECRET` environment variable!

Generate a secure secret:
```bash
openssl rand -base64 32
```

See the [JWT Configuration Fix Guide](JWT_CONFIG_FIX.md) for detailed information.

## Useful Keyboard Shortcuts

- Run: Shift+F10 (Win) / ⌃R (Mac)
- Debug: Shift+F9 (Win) / ⌃D (Mac)
- Stop: Ctrl+F2 (Win) / ⌘F2 (Mac)
- Rerun: Ctrl+F5 (Win) / ⌘R (Mac)
- Build: Ctrl+F9 (Win) / ⌘F9 (Mac)

## Testing in IntelliJ

### Run All Tests
- Right-click on `src/test/java` → Run 'All Tests'
- Or: Run → Run → Edit Configurations → "+" → JUnit

### Run Single Test
- Open test class
- Click green arrow next to class/method
- Or right-click → Run 'TestName'

## API Testing

### Using Swagger UI
1. Run the application
2. Open browser: http://localhost:8080/swagger-ui.html

### Using Postman
- Import `postman_collection.json` from project root

### Using IntelliJ HTTP Client
Create `.http` files in your project and use IntelliJ's built-in HTTP client.

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [IntelliJ IDEA Spring Boot Guide](https://www.jetbrains.com/help/idea/spring-boot.html)
- [Project README](../README.md)
- [Implementation Details](../IMPLEMENTATION.md)

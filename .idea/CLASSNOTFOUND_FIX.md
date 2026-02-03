# ClassNotFoundException Fix Guide

## Problem
```
Error: Could not find or load main class com.focusapp.backend.FocusAppBackendApplication
Caused by: java.lang.ClassNotFoundException: com.focusapp.backend.FocusAppBackendApplication
```

## Root Cause

This error occurs when IntelliJ tries to run the application but cannot find the compiled `.class` files. This happens because:

1. **Project hasn't been compiled yet** - The source code exists but hasn't been compiled to bytecode
2. **Target directory is missing** - Maven compiles classes to `target/classes/` but this directory doesn't exist
3. **IntelliJ's build process hasn't run** - The IDE didn't build the project before attempting to run

## Solutions

### Solution 1: Compile with Maven (Recommended)

This is the most reliable solution:

```bash
# Navigate to project directory
cd /path/to/focusappbackend

# Clean and compile
mvn clean compile
```

**What this does:**
- Cleans the `target/` directory
- Downloads all dependencies
- Compiles all Java source files to `target/classes/`
- Processes resources

After compilation, you should see:
```
[INFO] BUILD SUCCESS
```

### Solution 2: Build in IntelliJ IDEA

Use IntelliJ's build system:

1. **Build → Build Project** (Ctrl+F9 / ⌘F9)
   - Compiles changed files only
   
2. **Build → Rebuild Project**
   - Compiles entire project from scratch
   - Use this if Build Project doesn't work

### Solution 3: Run Maven Lifecycle

Using IntelliJ's Maven tool window:

1. Open Maven tool window (View → Tool Windows → Maven)
2. Expand Lifecycle
3. Double-click **compile**
4. Wait for completion

### Solution 4: Update Run Configuration

Ensure the run configuration compiles before running:

1. Click run configuration dropdown → Edit Configurations
2. Select "FocusAppBackendApplication"
3. In "Before launch" section, ensure you have:
   - **Maven goal: compile** 
   - **Build** (or Make)
4. Click Apply

The updated configuration file should include:
```xml
<method v="2">
  <option name="Maven.BeforeRunTask" enabled="true" file="$PROJECT_DIR$/pom.xml" goal="compile" />
  <option name="Make" enabled="true" />
</method>
```

### Solution 5: Reload Maven Project

If Maven dependencies aren't properly loaded:

1. Right-click on `pom.xml`
2. Maven → Reload Project
3. Wait for reimport to complete
4. Try compiling again

## Verification

After applying a solution, verify the fix:

### 1. Check target directory exists
```bash
ls -la target/classes/com/focusapp/backend/
```

You should see:
```
FocusAppBackendApplication.class
config/
controller/
dto/
...
```

### 2. Run the application
```bash
# Using Maven
mvn spring-boot:run

# Or using java directly
java -jar target/focus-backend-1.0.0.jar
```

### 3. Check logs
Successful startup shows:
```
Started FocusAppBackendApplication in X.XXX seconds
Tomcat started on port(s): 8080 (http)
```

## Prevention

To prevent this issue in the future:

### 1. Always compile after clone
```bash
git clone https://github.com/boracanbaltaci/focusappbackend.git
cd focusappbackend
mvn clean compile  # ← Important!
```

### 2. Enable auto-build in IntelliJ
- Settings → Build, Execution, Deployment → Compiler
- Check "Build project automatically"

### 3. Use Maven for builds
Instead of just clicking Run:
1. Run Maven compile first
2. Then run the application

### 4. Add to .gitignore
The `target/` directory should already be in `.gitignore` because:
- It contains generated/compiled files
- Each developer generates their own
- Should not be committed to version control

## Common Variations of This Error

### "Error: Could not find or load main class"
Same root cause - compiled classes missing

### "ClassNotFoundException" for other classes
- Same solution: compile the project
- Could also indicate missing dependencies (run `mvn dependency:resolve`)

### "NoClassDefFoundError"
- Class compiled but dependency missing
- Run `mvn clean install` to ensure all dependencies

## Additional Debugging

If none of the above works:

### 1. Clean everything
```bash
mvn clean
rm -rf target/
rm -rf ~/.m2/repository/com/focusapp/
mvn compile
```

### 2. Check Java version
```bash
java -version  # Should be Java 17
mvn -version   # Should use Java 17
```

In IntelliJ:
- File → Project Structure → Project → SDK: 17
- File → Project Structure → Modules → Language Level: 17

### 3. Check for compilation errors
```bash
mvn compile
```

Look for `[ERROR]` lines indicating compilation failures.

### 4. Invalidate IntelliJ caches
- File → Invalidate Caches / Restart
- Choose "Invalidate and Restart"

## Summary

**Quick Fix:**
```bash
mvn clean compile
```

**Long-term Fix:**
- Update run configuration to include Maven compile step
- Enable auto-build in IntelliJ
- Always compile after cloning repository

## Related Documentation

- [IntelliJ Setup Guide](INTELLIJ_SETUP.md)
- [Turkish Quick Start](TURKISH_QUICKSTART.md)
- [Main README](../README.md)

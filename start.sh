#!/bin/bash

# Focus App Backend - Quick Start Script

echo "🚀 Starting Focus App Backend..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

# Check if .env exists, if not copy from .env.example
if [ ! -f .env ]; then
    echo "📋 Creating .env from .env.example..."
    cp .env.example .env
    echo "⚠️  Please update .env with your configuration"
fi

# Load environment variables
if [ -f .env ]; then
    echo "📦 Loading environment variables..."
    export $(cat .env | grep -v '^#' | xargs)
fi

# Build the application if JAR doesn't exist
if [ ! -f target/focus-backend-1.0.0.jar ]; then
    echo "🔨 Building application..."
    mvn clean package -DskipTests
fi

# Start the application
echo "✅ Starting application on port ${SERVER_PORT:-8080}..."
java -jar target/focus-backend-1.0.0.jar

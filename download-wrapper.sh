#!/bin/bash
# Download Gradle wrapper JAR file
# This script downloads the gradle-wrapper.jar needed for the Gradle wrapper

VERSION="8.2"
WRAPPER_JAR_URL="https://raw.githubusercontent.com/gradle/gradle/v${VERSION}/gradle/wrapper/gradle-wrapper.jar"
WRAPPER_JAR_PATH="gradle/wrapper/gradle-wrapper.jar"

echo "Downloading Gradle wrapper JAR (version ${VERSION})..."
mkdir -p "$(dirname "$WRAPPER_JAR_PATH")"

if curl -L -o "$WRAPPER_JAR_PATH" "$WRAPPER_JAR_URL"; then
    echo "✓ Successfully downloaded gradle-wrapper.jar"
else
    echo "✗ Failed to download gradle-wrapper.jar"
    echo "Alternative: Run 'gradle wrapper' if you have Gradle installed"
    exit 1
fi


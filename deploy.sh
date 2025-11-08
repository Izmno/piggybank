#!/bin/bash

# Firebase App Distribution Deployment Script
# Usage: ./deploy.sh [debug|release] [release-notes]

set -e

BUILD_TYPE="${1:-release}"
RELEASE_NOTES="${2:-New build available for testing}"

if [ "$BUILD_TYPE" != "debug" ] && [ "$BUILD_TYPE" != "release" ]; then
    echo "Error: Build type must be 'debug' or 'release'"
    echo "Usage: ./deploy.sh [debug|release] [release-notes]"
    exit 1
fi

# Capitalize first letter for Gradle task names
BUILD_TYPE_CAPITALIZED=$(echo "$BUILD_TYPE" | awk '{print toupper(substr($0,1,1)) tolower(substr($0,2))}')

echo "Building $BUILD_TYPE APK..."
./gradlew assemble$BUILD_TYPE_CAPITALIZED

echo "Uploading to Firebase App Distribution..."
./gradlew appDistributionUpload$BUILD_TYPE_CAPITALIZED -PreleaseNotes="$RELEASE_NOTES"

echo "✓ Build uploaded successfully!"
echo "Testers will receive a notification via Firebase App Tester app."


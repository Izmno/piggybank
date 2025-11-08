#!/bin/bash

# Firebase App Distribution Deployment Script
# Usage: ./deploy.sh [release-notes]

set -e

RELEASE_NOTES="${1:-New build available for testing}"

echo "Building release APK..."
./gradlew assembleRelease

echo "Uploading to Firebase App Distribution..."
./gradlew appDistributionUploadRelease -PreleaseNotes="$RELEASE_NOTES"

echo "✓ Build uploaded successfully!"
echo "Testers will receive a notification via Firebase App Tester app."


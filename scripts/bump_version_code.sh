#!/bin/bash

# Script to bump the version code in app/build.gradle.kts
# Increments the version code by 1

set -e

BUILD_FILE="app/build.gradle.kts"

if [ ! -f "$BUILD_FILE" ]; then
    echo "Error: $BUILD_FILE not found"
    exit 1
fi

# Extract current version code
CURRENT_VERSION=$(grep -E "^\s*versionCode\s*=" "$BUILD_FILE" | sed -E 's/.*versionCode\s*=\s*([0-9]+).*/\1/')

if [ -z "$CURRENT_VERSION" ]; then
    echo "Error: Could not find versionCode in $BUILD_FILE"
    exit 1
fi

# Increment version code
NEW_VERSION=$((CURRENT_VERSION + 1))

echo "Bumping version code from $CURRENT_VERSION to $NEW_VERSION"

# Update the file using sed (works on both macOS and Linux)
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS sed requires -i '' for in-place editing
    sed -i '' "s/versionCode = $CURRENT_VERSION/versionCode = $NEW_VERSION/" "$BUILD_FILE"
else
    # Linux sed
    sed -i "s/versionCode = $CURRENT_VERSION/versionCode = $NEW_VERSION/" "$BUILD_FILE"
fi

echo "✓ Version code updated to $NEW_VERSION"


# PiggyBank - Android Counter App

A simple Android app that displays a button and counts how many times it has been pressed.

## Requirements

- Android SDK (API 24 or higher)
- Java 17 or higher
- Android device or emulator with USB debugging enabled (for installation)

## Initial Setup

If you've cloned this repository and don't have `gradle/wrapper/gradle-wrapper.jar`, download it first:

```bash
./download-wrapper.sh
```

Alternatively, if you have Gradle installed, you can run:
```bash
gradle wrapper
```

## Building the App

### Build APK

To build a debug APK:

```bash
./gradlew assembleDebug
```

The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Build Release APK

To build a release APK (requires signing configuration):

```bash
./gradlew assembleRelease
```

## Installing to Your Phone

### Option 1: Using Gradle (Recommended)

Connect your Android device via USB with USB debugging enabled, then run:

```bash
./gradlew installDebug
```

This will build and install the app directly to your connected device.

**Prerequisites:**
- Enable Developer Options on your Android device
- Enable USB debugging in Developer Options
- Install ADB (Android Debug Bridge) - usually comes with Android SDK
- Accept the USB debugging prompt on your device when first connecting

### Option 2: Manual Installation

1. Build the APK using `./gradlew assembleDebug`
2. Transfer `app/build/outputs/apk/debug/app-debug.apk` to your phone
3. On your phone, enable "Install from Unknown Sources" in Settings
4. Open the APK file on your phone and install it

### Option 3: Using Android Studio

1. Open the project in Android Studio
2. Connect your device via USB (with USB debugging enabled)
3. Click the "Run" button (green play icon) or press `Shift + F10`

## Project Structure

```
piggybank/
├── app/
│   ├── build.gradle.kts          # App-level build configuration
│   └── src/main/
│       ├── AndroidManifest.xml   # App manifest
│       ├── java/be/izmno/piggybank/
│       │   └── MainActivity.kt   # Main activity with button click handler
│       └── res/
│           ├── layout/
│           │   └── activity_main.xml  # UI layout
│           └── values/
│               ├── strings.xml   # String resources
│               └── themes.xml    # App theme
├── build.gradle.kts              # Root-level build configuration
├── settings.gradle.kts           # Project settings
├── gradle.properties             # Gradle properties
├── download-wrapper.sh           # Script to download gradle-wrapper.jar
└── gradlew                       # Gradle wrapper script

```

## Features

- Simple button interface
- Click counter that increments on each button press
- Counter state is preserved when the screen rotates (using `onSaveInstanceState`)

## Development

### Using Android Studio

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to this directory and select it
4. Wait for Gradle sync to complete
5. Run the app on an emulator or connected device

### Using Command Line

Build commands:
- `./gradlew build` - Build the entire project
- `./gradlew assembleDebug` - Build debug APK
- `./gradlew installDebug` - Build and install to connected device
- `./gradlew clean` - Clean build artifacts


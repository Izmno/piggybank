plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
}

// Function to get git commit count for versionCode
fun getVersionCode(): Int {
    return try {
        val process = ProcessBuilder("git", "rev-list", "--count", "HEAD")
            .directory(project.rootDir)
            .start()
        process.waitFor()
        val output = process.inputStream.bufferedReader().readText().trim()
        if (output.isNotEmpty()) {
            output.toInt()
        } else {
            1
        }
    } catch (e: Exception) {
        1
    }
}

android {
    namespace = "be.izmno.piggybank"
    compileSdk = 34

    defaultConfig {
        applicationId = "be.izmno.piggybank"
        minSdk = 24
        targetSdk = 34
        versionCode = getVersionCode()
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val keystorePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
            val envKeyPassword = System.getenv("KEY_PASSWORD") ?: System.getenv("KEYSTORE_PASSWORD") ?: ""
            val keystoreAlias = System.getenv("KEYSTORE_ALIAS") ?: "piggybank"
            val keystoreFile = file("piggybank-release.keystore")

            if (keystoreFile.exists() && keystorePassword.isNotEmpty()) {
                storeFile = keystoreFile
                storePassword = keystorePassword
                keyAlias = keystoreAlias
                keyPassword = envKeyPassword
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

firebaseAppDistribution {
    releaseNotes = project.findProperty("releaseNotes") as String? ?: "New build available for testing"
    groups = "izmno-admin"
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    
    // Fragment KTX
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    
    // GridLayout
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    
    // Gson for JSON serialization
    implementation("com.google.code.gson:gson:2.10.1")
}


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.insubria.cookingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.insubria.cookingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Dipendenze per Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.firebase.auth.ktx)
    ksp("androidx.room:room-compiler:2.6.1") // Sostituisci kapt con ksp
    implementation("androidx.room:room-ktx:2.6.1")

    // Dipendenze AndroidX Core
    implementation("androidx.core:core-ktx:1.12.0")

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui-tooling:1.5.1")
    implementation("androidx.compose.material3:material3:1.1.0")

    // Dipendenze di test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")

    // Dipendenze per il debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp) // Use alias from libs.versions.toml
    alias(libs.plugins.google.dagger.hilt) // Use alias from libs.versions.toml
}

android {
    namespace = "com.example.gestiontienda2"
    compileSdk = 34 // Changed from 35

    defaultConfig {
        applicationId = "com.example.gestiontienda2"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Room + KSP
    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler.ksp) // Correct KSP for Room

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler) // Correct KSP for Hilt (alias changed)

    // Firebase
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.play.services.analytics.impl) // Keep if used
    implementation(libs.ads.mobile.sdk) // Keep if used

    // Navigation
    implementation(libs.androidx.navigation.runtime.android) // Keep if used
    implementation(libs.androidx.navigation.runtime.jvmstubs) // Keep if used
    implementation(libs.androidx.navigation.runtime.desktop) // Keep if used

    // Camera
    implementation(libs.androidx.camera.core) // Keep if used

    // Jetpack Compose
    implementation(libs.androidx.ui) // Keep one
    implementation(libs.androidx.material3) // Keep one (alias changed)
    implementation(libs.androidx.activity.compose) // Keep one
    implementation(libs.androidx.ui.tooling.preview.android) // Keep if used
    implementation(libs.androidx.glance.preview) // Keep if used
    implementation(libs.androidx.tiles.tooling.preview) // Keep if used

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx) // Keep one
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Use common alias
    implementation(libs.androidx.lifecycle.livedata.ktx) // Use common alias

    // Core & AppCompat (View system, keep if any XML layouts/views are used, or by other dependencies)
    implementation(libs.androidx.core.ktx) // Keep one
    implementation(libs.androidx.appcompat) // Keep one
    implementation(libs.material) // View system material components, keep if used

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // implementation(libs.kotlin.stdlib) // Remove, provided by Kotlin plugin
    // androidx.room.runtime.android was removed from toml, so removed here too
}

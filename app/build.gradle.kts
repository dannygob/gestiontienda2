plugins {
    alias(libs.plugins.android.application)
    //id("com.android.application")
    //alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // Or id("org.jetbrains.kotlin.android")
    alias(libs.plugins.ksp)
    //alias(libs.plugins.google.dagger.hilt.android) // Or your Hilt plugin alias


}

android {
    namespace = "com.example.gestiontienda2"
    compileSdk = 35

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

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    // 🔹 Jetpack Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.ui.tooling.preview.android)

    // 🔹 Navigation
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.navigation.compose)

    // 🔹 Room (con KSP)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler.ksp)

    // 🔹 Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // 🔹 Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore.ktx)

    // 🔹 Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // 🔹 Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // 🔹 Google Play Services / Ads
    implementation(libs.play.services.analytics.impl)
    implementation(libs.ads.mobile.sdk)

    // 🔹 CameraX
    implementation(libs.androidx.camera.core)

    // 🔹 Glance & Tiles
    implementation(libs.androidx.glance.preview)
    implementation(libs.androidx.tiles.tooling.preview)

    // 🔹 Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.kotlin.stdlib)

    // 🔹 Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.espresso.core)



    implementation(libs.androidx.room.runtime.v260)
    annotationProcessor(libs.androidx.room.compiler.v260) // For Java Annotation Processing
    // Or if you are using KSP for Room:
    ksp(libs.androidx.room.compiler.v260)

    val composeBom = platform("androidx.compose:compose-bom:2024.04.01") // Or latest
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.ui)
    // ... other Compose dependencies

    implementation(libs.hilt.android.v2511) // Or latest


    implementation(libs.androidx.hilt.navigation.compose) // Or the latest stable version




}

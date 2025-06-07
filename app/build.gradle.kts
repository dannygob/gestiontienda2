plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.kotlin.ksp) // ✅ Migrado a KSP
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
//    id("com.android.application")
    id("com.google.gms.google-services")
  
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

    // 🔹 Hilt (con KSP)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // 🔹 Firebase
    implementation(libs.firebase.firestore.ktx)

    // 🔹 Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // 🔹 Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // 🔹 Google Play / Ads / Analytics
    implementation(libs.play.services.analytics.impl)
    implementation(libs.ads.mobile.sdk)

    // 🔹 CameraX
    implementation(libs.androidx.camera.core)

    // 🔹 Preview / Glance
    implementation(libs.androidx.glance.preview)
    implementation(libs.androidx.tiles.tooling.preview)

    // 🔹 Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // 🔹 Kotlin Stdlib
    implementation(libs.kotlin.stdlib)

    // 🔹 Test (si aplican)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)



}


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //alias(libs.plugins.kotlin.ksp) // ✅ Migrado a KSP
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
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
    // Room + KSP
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.firebase.firestore.ktx)
    ksp(libs.room.compiler.ksp) // ✅ KSP en lugar de kapt
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")

    // Jetpack Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")




}

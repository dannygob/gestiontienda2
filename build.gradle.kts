// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
//    id 'com.android.application'
   //id ("org.jetbrains.kotlin.android")
//    id 'kotlin-kapt' // <-- este es importante
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    id("com.google.dagger.hilt.android") version "2.56.2" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

    //id ("org.jetbrains.kotlin.android") version "2.0.21" apply false
   // id ("com.android.application' version '8.2.0") apply false
}
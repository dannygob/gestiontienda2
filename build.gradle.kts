
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.compose) apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    //id("com.google.devtools.ksp") version "2.56.2" apply false // Use the latest compatible KSP version
    // id("com.google.dagger.hilt.android") version "2.56.2" apply false // Use the latest Hilt version

}

pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false

    }
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
//        maven {
//            url = uri("https://plugins.gradle.org/m2/")
    }
    }


rootProject.name = "gestiontienda2"
include(":app")

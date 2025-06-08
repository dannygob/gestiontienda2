pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false

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

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    // No necesitas declarar plugins con id(...) si usas alias en libs.versions.toml
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "gestiontienda2"
include(":app")

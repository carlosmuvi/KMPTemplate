pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "KMPTemplate"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app", ":common")

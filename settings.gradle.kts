pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "automation-orchestrator"

include("domain")
include("infrastructure:application")
include("infrastructure:rest-api")
include("infrastructure:rest-api:open-api")
include("infrastructure:jdbc-storage-connector")
pluginManagement {

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
}

rootProject.name = "gRPCKeyValueServer"

fun includeProject(name: String, projectPath: String? = null) {
    include(name)
    projectPath?.let { project(name).projectDir = File(it) }
}

includeProject(":domain", "src/domain")
includeProject(":application", "src/application")
includeProject(":presentation", "src/presentation")
includeProject(":infrastructure:boot", "src/infrastructure/boot")
includeProject(":infrastructure:provider", "src/infrastructure/provider")

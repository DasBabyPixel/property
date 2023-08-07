import org.gradle.api.initialization.resolve.RepositoriesMode

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {
    repositories {
        maven {
            name = "DarkCube"
            url = uri("https://nexus.darkcube.eu/repository/dasbabypixel")
        }
        mavenCentral()
    }
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
}

rootProject.name = "property"

plugins {
    id("java-library")
    id("maven-publish")
}

java {
    withSourcesJar()
    withJavadocJar()
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(8))

group = "de.dasbabypixel.api"
version = "2.0.0-rc26"

tasks {
    jar {
        manifest.attributes.put("Automatic-Module-Name", "de.dasbabypixel.property")
    }
}

publishing {
    publications {
        register("java", MavenPublication::class) {
            from(components.getByName("java"))
        }
    }
    repositories {
        maven {
            name = "DarkCube"
            credentials(PasswordCredentials::class)
            url = uri("https://nexus.darkcube.eu/repository/dasbabypixel/")
        }
    }
}

dependencies {
    implementation("de.dasbabypixel:annotations:0.1")
}

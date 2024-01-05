plugins {
    kotlin("jvm") version "1.9.0"
}

group = "me.kimihiqq"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "kotlin")

    dependencies {
        implementation(kotlin("stdlib"))
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")

        testImplementation("org.mockito:mockito-core:3.11.2")
    }

    tasks.test {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
    }
}

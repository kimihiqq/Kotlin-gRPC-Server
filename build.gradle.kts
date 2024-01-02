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
    }

    tasks.test {
        useJUnitPlatform()
    }
}

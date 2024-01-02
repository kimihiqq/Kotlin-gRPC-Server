dependencies {
    implementation(project(":domain"))

    runtimeOnly("mysql:mysql-connector-java:8.0.29")

    testImplementation("org.testcontainers:testcontainers:1.17.3")
    testImplementation("org.testcontainers:mysql:1.17.3")
}

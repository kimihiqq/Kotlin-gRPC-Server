dependencies {
    implementation(project(":domain"))
    implementation("com.zaxxer:HikariCP:4.0.3")

    runtimeOnly("mysql:mysql-connector-java:8.0.29")

    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.testcontainers:mysql:1.17.3")
    testImplementation("org.testcontainers:junit-jupiter:1.17.3")
}

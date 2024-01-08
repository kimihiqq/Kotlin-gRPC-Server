dependencies {
    implementation(project(":domain"))
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.hibernate:hibernate-core:5.6.10.Final")
    implementation("javax.persistence:javax.persistence-api:2.2")
    implementation("com.h2database:h2:2.2.220")

    runtimeOnly("mysql:mysql-connector-java:8.0.29")

    testImplementation("org.testcontainers:mysql:1.17.3")
    testImplementation("org.testcontainers:junit-jupiter:1.17.3")
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("com.h2database:h2:2.2.220")
}

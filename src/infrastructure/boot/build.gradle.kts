plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("me.kimihiqq.boot.MainKt")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":presentation"))
    implementation(project(":infrastructure:provider"))
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("io.grpc:grpc-netty:1.59.0")
    implementation("io.grpc:grpc-stub:1.59.0")
    implementation("io.grpc:grpc-protobuf:1.59.0")
    implementation("org.hibernate:hibernate-entitymanager:5.6.10.Final")

    runtimeOnly("mysql:mysql-connector-java:8.0.29")

    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.testcontainers:mysql:1.17.3")
    testImplementation("org.testcontainers:junit-jupiter:1.17.3")
    testImplementation("io.grpc:grpc-testing:1.59.0")
}

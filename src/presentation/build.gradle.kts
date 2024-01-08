import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.8.17"
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    implementation("io.grpc:grpc-stub:1.59.0")
    implementation("io.grpc:grpc-protobuf:1.59.0")
    implementation("com.google.protobuf:protobuf-java:3.25.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    testImplementation("org.mockito:mockito-inline:4.11.0")
    testImplementation("io.grpc:grpc-testing:1.59.0")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.60.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

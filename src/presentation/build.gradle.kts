import com.google.protobuf.gradle.*

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.8.17"
}


dependencies {
    implementation(project(":application"))

    implementation("io.grpc:grpc-kotlin-stub:1.4.0")
    implementation("io.grpc:grpc-protobuf:1.59.1")
    implementation("io.grpc:grpc-netty:1.60.0")
    implementation("com.google.protobuf:protobuf-java:3.25.1")

    testImplementation("io.grpc:grpc-testing:1.60.0")
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.43.2"
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

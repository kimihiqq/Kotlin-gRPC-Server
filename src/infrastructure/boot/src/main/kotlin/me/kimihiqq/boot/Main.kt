package me.kimihiqq.boot

import io.grpc.ServerBuilder
import me.kimihiqq.application.service.KeyValueService
import me.kimihiqq.presentation.Intercepter.KeyValidationInterceptor
import me.kimihiqq.presentation.serviceImpl.KeyValueServiceImpl
import me.kimihiqq.provider.config.EntityManagerConfig
import me.kimihiqq.provider.repository.impl.JpaKeyValueRepository
import java.util.concurrent.Executors

fun main() {

    EntityManagerConfig.initialize("KeyValuePersistenceUnit")

    val keyValueRepository = JpaKeyValueRepository()
    val keyValueService = KeyValueService(keyValueRepository)
    val keyValueServiceImpl = KeyValueServiceImpl(keyValueService)
    val executorService = Executors.newFixedThreadPool(10)
    val server = ServerBuilder
        .forPort(8080)
        .addService(keyValueServiceImpl)
        .executor(executorService)
        .intercept(KeyValidationInterceptor())
        .build()

    server.start()
    println("Server started, listening on 8080")
    server.awaitTermination()
}

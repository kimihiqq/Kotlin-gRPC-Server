package me.kimihiqq.boot

import io.grpc.ServerBuilder
import me.kimihiqq.application.service.KeyValueService
import me.kimihiqq.boot.config.DataSourceConfig
import me.kimihiqq.presentation.serviceImpl.KeyValueServiceImpl
import me.kimihiqq.provider.repository.impl.JdbcKeyValueRepository

fun main() {
    val dataSourceConfig = DataSourceConfig()
    val dataSource = dataSourceConfig.createDataSource()
    val keyValueRepository = JdbcKeyValueRepository(dataSource)
    val keyValueService = KeyValueService(keyValueRepository)
    val keyValueServiceImpl = KeyValueServiceImpl(keyValueService)
    val server = ServerBuilder
        .forPort(8080)
        .addService(keyValueServiceImpl)
        .build()

    server.start()
    println("Server started, listening on 8080")
    server.awaitTermination()
}

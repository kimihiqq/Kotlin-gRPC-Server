package me.kimihiqq.boot

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Server
import io.grpc.ServerBuilder
import me.kimihiqq.application.service.KeyValueService
import me.kimihiqq.boot.config.TestEntityManagerConfig
import me.kimihiqq.grpc.GetRequest
import me.kimihiqq.grpc.KeyValueServiceGrpc
import me.kimihiqq.grpc.SaveRequest
import me.kimihiqq.presentation.serviceImpl.KeyValueServiceImpl
import me.kimihiqq.provider.config.EntityManagerConfig
import me.kimihiqq.provider.repository.impl.JpaKeyValueRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.Executors

@Testcontainers
class KeyValueServiceIntegrationTest {
    companion object {
        private lateinit var server: Server
        private lateinit var channel: ManagedChannel

        val dockerComposeFile = File("../../../docker-compose-test.yaml")

        @Container
        val environment = DockerComposeContainer(dockerComposeFile)
            .withExposedService("local-db", 3306, Wait.forLogMessage(".*ready for connections.*", 1))
            .withExposedService("local-db-migrate", 0, Wait.forLogMessage("(.*Successfully applied.*)|(.*Successfully validated.*)", 1))

        @BeforeAll
        @JvmStatic
        fun setUp() {
            environment.start()

            TestEntityManagerConfig(environment).initialize()

            val keyValueRepository = JpaKeyValueRepository()
            val keyValueService = KeyValueService(keyValueRepository)
            val executorService = Executors.newFixedThreadPool(10)

            server = ServerBuilder
                .forPort(8080)
                .addService(KeyValueServiceImpl(keyValueService))
                .executor(executorService)
                .build()
                .start()

            channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build()
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            channel.shutdownNow()
            server.shutdownNow()
            environment.stop()
        }
    }

    @Test
    fun `테스트 - 병렬 요청 처리`() {
        val stub = KeyValueServiceGrpc.newBlockingStub(channel)
        val executor = Executors.newFixedThreadPool(20)

        val tasks = (1..100).map { index ->
            Callable {
                val key = "key_$index"
                val value = "value_$index"

                stub.save(SaveRequest.newBuilder().setKey(key).setValue(value).build())

                val response = stub.get(GetRequest.newBuilder().setKey(key).build())
                assertEquals(key, response.key)
                assertEquals(value, response.value)
            }
        }

        val futures = tasks.map(executor::submit)
        futures.forEach { it.get() }

        executor.shutdown()
    }
}

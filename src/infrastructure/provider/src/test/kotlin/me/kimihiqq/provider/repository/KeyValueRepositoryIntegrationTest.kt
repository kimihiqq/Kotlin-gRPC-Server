package me.kimihiqq.provider.repository

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.AfterAll
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import javax.sql.DataSource
import me.kimihiqq.domain.KeyValue.KeyValue
import me.kimihiqq.provider.config.TestDataSourceConfig
import me.kimihiqq.provider.repository.impl.JdbcKeyValueRepository
import org.junit.jupiter.api.Assertions.*
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
class JdbcKeyValueRepositoryIntegrationTest {

    companion object {
        private lateinit var dataSource: DataSource
        private lateinit var repository: JdbcKeyValueRepository

        val dockerComposeFile = File("../../../docker-compose-test.yaml")

        @Container
        val environment = DockerComposeContainer(dockerComposeFile)
            .withExposedService("local-db", 3306, Wait.forLogMessage(".*ready for connections.*", 1)
            )
            .withExposedService("local-db-migrate", 0, Wait.forLogMessage("(.*Successfully applied.*)|(.*Successfully validated.*)", 1))

        @BeforeAll
        @JvmStatic
        fun setUp() {
            environment.start()

            val testDataSourceConfig = TestDataSourceConfig(environment)
            dataSource = testDataSourceConfig.createDataSource()

            repository = JdbcKeyValueRepository(dataSource)
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            environment.stop()
        }
    }

    @Test
    fun `save 메서드가 키-값 쌍을 데이터베이스에 저장한다`() {
        val keyValue = KeyValue("testKey", "testValue")
        val savedKeyValue = repository.save(keyValue)

        assertEquals(keyValue, savedKeyValue)
    }

    @Test
    fun `findByKey 메서드가 존재하는 키에 대한 값을 정확히 검색한다`() {
        val keyValue = KeyValue("testKey", "testValue")
        repository.save(keyValue)

        val foundKeyValue = repository.findByKey("testKey")

        assertNotNull(foundKeyValue)
        assertEquals(keyValue, foundKeyValue)
    }

    @Test
    fun `deleteByKey 메서드가 존재하는 키를 정확히 삭제한다`() {
        val keyValue = KeyValue("testKey", "testValue")
        repository.save(keyValue)

        val deleteResult = repository.deleteByKey("testKey")
        assertTrue(deleteResult)

        val foundKeyValue = repository.findByKey("testKey")
        assertNull(foundKeyValue)
    }
}

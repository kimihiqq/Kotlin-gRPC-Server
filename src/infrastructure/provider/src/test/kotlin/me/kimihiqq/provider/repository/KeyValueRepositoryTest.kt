package me.kimihiqq.provider.repository

import me.kimihiqq.domain.KeyValue.KeyValue
import me.kimihiqq.provider.config.EntityManagerConfig
import me.kimihiqq.provider.repository.impl.JpaKeyValueRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.persistence.EntityManager
import javax.persistence.Persistence

class JpaKeyValueRepositoryTest {

    private lateinit var entityManager: EntityManager
    private lateinit var repository: JpaKeyValueRepository

    @BeforeEach
    fun setUp() {

        EntityManagerConfig.initialize("TestKeyValuePersistenceUnit")

        entityManager = EntityManagerConfig.createEntityManager()
        repository = JpaKeyValueRepository()
    }

    @AfterEach
    fun tearDown() {
        entityManager.close()
    }

    @Test
    fun `save 메서드가 키-값 쌍을 데이터베이스에 저장한다`() {
        val keyValue = KeyValue("testKey", "testValue")
        repository.save(keyValue)

        val foundKeyValue = entityManager.find(KeyValue::class.java, "testKey")
        assertNotNull(foundKeyValue)
        assertEquals("testValue", foundKeyValue?.value)
    }

    @Test
    fun `findByKey 메서드가 존재하는 키에 대한 값을 정확히 검색한다`() {
        val keyValue = KeyValue("testKey", "testValue")
        repository.save(keyValue)

        val foundKeyValue = repository.findByKey("testKey")
        assertNotNull(foundKeyValue)
        assertEquals("testValue", foundKeyValue?.value)
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

    @Test
    fun `findByKey 메서드가 존재하지 않는 키에 대해 null을 반환한다`() {
        val nonExistingKey = "nonExistingKey"
        val result = repository.findByKey(nonExistingKey)
        assertNull(result)
    }

    @Test
    fun `deleteByKey 메서드가 존재하지 않는 키를 삭제하려는 경우 false를 반환한다`() {
        val nonExistingKey = "nonExistingKey"
        val result = repository.deleteByKey(nonExistingKey)
        assertFalse(result)
    }
}

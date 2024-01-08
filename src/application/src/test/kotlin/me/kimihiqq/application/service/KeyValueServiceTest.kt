package me.kimihiqq.application.service

import me.kimihiqq.application.dto.KeyValueDto
import me.kimihiqq.domain.KeyValue.KeyValue
import me.kimihiqq.domain.KeyValue.KeyValueRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*

fun <T> anyNonNull(): T {
    Mockito.any<T>()
    return null as T
}

class KeyValueServiceTest {

    private lateinit var keyValueService: KeyValueService
    private val keyValueRepository: KeyValueRepository = mock(KeyValueRepository::class.java)

    @BeforeEach
    fun setUp() {
        keyValueService = KeyValueService(keyValueRepository)
    }

    @Test
    fun `saveKeyValue 메서드가 정상적으로 작동한다`() {
        val dto = KeyValueDto("testKey", "testValue")
        val keyValue = KeyValue("testKey", "testValue")

        `when`(keyValueRepository.save(anyNonNull())).thenReturn(keyValue)

        val savedKeyValueDto = keyValueService.saveKeyValue(dto)
        assertNotNull(savedKeyValueDto)
        assertEquals(dto, savedKeyValueDto)
    }

    @Test
    fun `getKeyValue 메서드가 존재하는 키를 찾을 때 정확한 값을 반환한다`() {
        val key = "existingKey"
        val keyValue = KeyValue(key, "theValue")

        `when`(keyValueRepository.findByKey(key)).thenReturn(keyValue)

        val resultDto = keyValueService.getKeyValue(key)
        assertNotNull(resultDto)
        assertEquals(KeyValueDto.fromKeyValue(keyValue), resultDto)
    }

    @Test
    fun `deleteKeyValue 메서드가 존재하는 키를 삭제할 때 true를 반환한다`() {
        val key = "existingKey"

        `when`(keyValueRepository.deleteByKey(key)).thenReturn(true)

        val result = keyValueService.deleteKeyValue(key)
        assertTrue(result)
    }

    @Test
    fun `getKeyValue 메서드가 존재하지 않는 키를 조회할 때 null을 반환한다`() {
        val key = "nonExistingKey"
        `when`(keyValueRepository.findByKey(key)).thenReturn(null)

        val resultDto = keyValueService.getKeyValue(key)
        assertNull(resultDto)
    }

    @Test
    fun `deleteKeyValue 메서드가 존재하지 않는 키를 삭제할 때 false를 반환한다`() {
        val key = "nonExistingKey"
        `when`(keyValueRepository.deleteByKey(key)).thenReturn(false)

        val result = keyValueService.deleteKeyValue(key)
        assertFalse(result)
    }
}

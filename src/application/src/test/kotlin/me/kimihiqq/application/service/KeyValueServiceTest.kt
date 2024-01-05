package me.kimihiqq.application.service

import me.kimihiqq.application.dto.KeyValueDto
import me.kimihiqq.domain.KeyValue.KeyValue
import me.kimihiqq.domain.KeyValue.KeyValueRepository
import me.kimihiqq.domain.error.ErrorCode
import me.kimihiqq.domain.error.exception.BusinessException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
        assertEquals(dto, savedKeyValueDto)

        verify(keyValueRepository).save(KeyValueDto.toKeyValue(dto))
    }

    @Test
    fun `getKeyValue 메서드가 존재하는 키를 찾을 때 정확한 값을 반환한다`() {
        val key = "existingKey"
        val keyValue = KeyValue(key, "theValue")

        `when`(keyValueRepository.findByKey(key)).thenReturn(keyValue)

        val resultDto = keyValueService.getKeyValue(key)
        assertNotNull(resultDto)
        assertEquals(KeyValueDto.fromKeyValue(keyValue), resultDto)

        verify(keyValueRepository).findByKey(key)
    }

    @Test
    fun `deleteKeyValue 메서드가 존재하는 키를 삭제할 때 true를 반환한다`() {
        val key = "existingKey"

        `when`(keyValueRepository.deleteByKey(key)).thenReturn(true)

        val result = keyValueService.deleteKeyValue(key)
        assertTrue(result)

        verify(keyValueRepository).deleteByKey(key)
    }

    @Test
    fun `saveKeyValue 메서드가 유효하지 않은 데이터로 실패한다`() {
        val invalidDto = KeyValueDto("", "testValue")

        assertThrows<BusinessException> {
            keyValueService.saveKeyValue(invalidDto)
        }.apply {
            assertEquals(ErrorCode.INVALID_INPUT, errorCode)
        }
    }

    @Test
    fun `getKeyValue 메서드가 존재하지 않는 키로 실패한다`() {
        val key = "nonExistingKey"
        `when`(keyValueRepository.findByKey(key)).thenReturn(null)

        assertThrows<BusinessException> {
            keyValueService.getKeyValue(key)
        }.apply {
            assertEquals(ErrorCode.KEY_NOT_FOUND, errorCode)
        }
    }

    @Test
    fun `deleteKeyValue 메서드가 존재하지 않는 키로 실패한다`() {
        val key = "nonExistingKey"
        `when`(keyValueRepository.deleteByKey(key)).thenReturn(false)

        assertThrows<BusinessException> {
            keyValueService.deleteKeyValue(key)
        }.apply {
            assertEquals(ErrorCode.KEY_NOT_FOUND, errorCode)
        }
    }
}

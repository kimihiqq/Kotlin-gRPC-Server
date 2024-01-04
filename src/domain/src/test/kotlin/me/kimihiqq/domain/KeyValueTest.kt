package me.kimihiqq.domain

import me.kimihiqq.domain.KeyValue.KeyValue
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class KeyValueTest {

    @Test
    fun `key가 비어있으면 예외를 발생시킨다`() {
        val exception = assertThrows<IllegalArgumentException> {
            KeyValue("", "some value")
        }
        assertEquals("키는 비어 있을 수 없습니다", exception.message)
    }

    @Test
    fun `key 길이가 50자를 초과하면 예외를 발생시킨다`() {
        val longKey = "a".repeat(51)
        val exception = assertThrows<IllegalArgumentException> {
            KeyValue(longKey, "some value")
        }
        assertEquals("키 길이는 50자를 넘을 수 없습니다", exception.message)
    }

    @Test
    fun `key에 알파벳, 숫자, 밑줄 이외의 문자가 포함되어 있으면 예외를 발생시킨다`() {
        val invalidKey = "invalid\$key"
        val exception = assertThrows<IllegalArgumentException> {
            KeyValue(invalidKey, "some value")
        }
        assertEquals("키는 알파벳, 숫자, 밑줄만 포함할 수 있습니다", exception.message)
    }

    @Test
    fun `유효한 key와 value를 가진 KeyValue 인스턴스가 정상적으로 생성된다`() {
        val keyValue = KeyValue("valid_key", "some value")
        assertNotNull(keyValue)
        assertEquals("valid_key", keyValue.key)
        assertEquals("some value", keyValue.value)
    }
}

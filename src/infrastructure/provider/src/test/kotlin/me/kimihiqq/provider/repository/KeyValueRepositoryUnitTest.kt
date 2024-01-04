package me.kimihiqq.provider.repository

import me.kimihiqq.domain.KeyValue.KeyValue
import me.kimihiqq.domain.KeyValue.KeyValueRepository
import me.kimihiqq.provider.repository.impl.JdbcKeyValueRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.sql.DataSource

class KeyValueRepositoryUnitTest {

    private val dataSource: DataSource = mock(DataSource::class.java)
    private val connection: Connection = mock(Connection::class.java)
    private val preparedStatement: PreparedStatement = mock(PreparedStatement::class.java)
    private val repository: KeyValueRepository = JdbcKeyValueRepository(dataSource)

    init {
        `when`(dataSource.connection).thenReturn(connection)
        `when`(connection.prepareStatement(anyString())).thenReturn(preparedStatement)
    }

    @Test
    fun `save 메서드가 정상적으로 작동한다`() {
        val keyValue = KeyValue("testKey", "testValue")

        `when`(preparedStatement.executeUpdate()).thenReturn(1)

        val savedKeyValue = repository.save(keyValue)
        assertEquals(keyValue, savedKeyValue)
    }

    @Test
    fun `findByKey 메서드가 존재하는 키를 정확히 찾아낸다`() {
        val key = "existingKey"
        val value = "theValue"
        val resultSet: ResultSet = mock(ResultSet::class.java)

        `when`(preparedStatement.executeQuery()).thenReturn(resultSet)
        `when`(resultSet.next()).thenReturn(true)
        `when`(resultSet.getString("key")).thenReturn(key)
        `when`(resultSet.getString("value")).thenReturn(value)

        val result = repository.findByKey(key)
        assertNotNull(result)
        assertEquals(key, result!!.key)
        assertEquals(value, result.value)
    }

    @Test
    fun `deleteByKey 메서드가 존재하는 키를 정확히 삭제한다`() {
        val key = "existingKey"
        `when`(preparedStatement.executeUpdate()).thenReturn(1)

        val result = repository.deleteByKey(key)
        assertTrue(result)
    }
}

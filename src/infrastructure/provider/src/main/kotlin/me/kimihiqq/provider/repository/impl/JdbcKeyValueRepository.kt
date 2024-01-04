package me.kimihiqq.provider.repository.impl

import me.kimihiqq.domain.KeyValue.KeyValue
import me.kimihiqq.domain.KeyValue.KeyValueRepository
import javax.sql.DataSource

class JdbcKeyValueRepository(private val dataSource: DataSource) : KeyValueRepository {

    override fun save(keyValue: KeyValue): KeyValue {
        dataSource.connection.use { conn ->

            val sql = "INSERT INTO KeyValue (`key`, `value`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `value` = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, keyValue.key)
                stmt.setString(2, keyValue.value)
                stmt.setString(3, keyValue.value)
                stmt.executeUpdate()
            }
        }
        return keyValue
    }

    override fun findByKey(key: String): KeyValue? {
        dataSource.connection.use { conn ->

            val sql = "SELECT `key`, `value` FROM KeyValue WHERE `key` = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, key)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        return KeyValue(rs.getString("key"), rs.getString("value"))
                    }
                }
            }
        }
        return null
    }

    override fun deleteByKey(key: String): Boolean {
        dataSource.connection.use { conn ->

            val sql = "DELETE FROM KeyValue WHERE `key` = ?"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, key)
                val rowsAffected = stmt.executeUpdate()
                return rowsAffected > 0
            }
        }
    }
}

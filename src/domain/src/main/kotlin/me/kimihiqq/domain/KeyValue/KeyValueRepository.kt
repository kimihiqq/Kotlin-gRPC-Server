package me.kimihiqq.domain.KeyValue

interface KeyValueRepository {
    fun save(keyValue: KeyValue): KeyValue
    fun findByKey(key: String): KeyValue?
    fun deleteByKey(key: String): Boolean
}

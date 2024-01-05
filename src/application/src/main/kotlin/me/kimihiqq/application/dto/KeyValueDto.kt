package me.kimihiqq.application.dto

import me.kimihiqq.domain.KeyValue.KeyValue

data class KeyValueDto(
    val key: String,
    val value: String?
) {
    companion object {
        fun fromKeyValue(keyValue: KeyValue): KeyValueDto {
            return KeyValueDto(keyValue.key, keyValue.value)
        }

        fun toKeyValue(keyValueDto: KeyValueDto): KeyValue {
            return KeyValue(keyValueDto.key, keyValueDto.value)
        }
    }
}

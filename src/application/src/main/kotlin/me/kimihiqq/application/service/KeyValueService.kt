package me.kimihiqq.application.service

import me.kimihiqq.application.dto.KeyValueDto
import me.kimihiqq.domain.KeyValue.KeyValueRepository
import org.slf4j.LoggerFactory

class KeyValueService(private val repository: KeyValueRepository) {

    private val logger = LoggerFactory.getLogger(KeyValueService::class.java)

    fun saveKeyValue(dto: KeyValueDto): KeyValueDto? {
        return try {
            val savedKeyValue = repository.save(KeyValueDto.toKeyValue(dto))
            KeyValueDto.fromKeyValue(savedKeyValue)
        } catch (e: Exception) {
            logger.error("KeyValue 저장 실패: 데이터베이스 오류, 키: ${dto.key}, 에러: ${e.message}")
            null
        }
    }

    fun getKeyValue(key: String): KeyValueDto? {
        return try {
            repository.findByKey(key)?.let { KeyValueDto.fromKeyValue(it) }
        } catch (e: Exception) {
            logger.error("KeyValue 조회 실패: 데이터베이스 오류, 키: $key, 에러: ${e.message}")
            null
        }
    }

    fun deleteKeyValue(key: String): Boolean {
        return try {
            repository.deleteByKey(key)
        } catch (e: Exception) {
            logger.error("KeyValue 삭제 실패: 데이터베이스 오류, 키: $key, 에러: ${e.message}")
            false
        }
    }
}

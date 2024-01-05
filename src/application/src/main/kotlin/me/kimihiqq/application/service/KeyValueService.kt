package me.kimihiqq.application.service

import me.kimihiqq.application.dto.KeyValueDto
import me.kimihiqq.domain.KeyValue.KeyValueRepository
import me.kimihiqq.domain.error.ErrorCode
import me.kimihiqq.domain.error.exception.BusinessException
import org.slf4j.LoggerFactory

class KeyValueService(private val repository: KeyValueRepository) {

    private val logger = LoggerFactory.getLogger(KeyValueService::class.java)

    fun saveKeyValue(dto: KeyValueDto): KeyValueDto {
        try {
            val savedKeyValue = repository.save(KeyValueDto.toKeyValue(dto))
            logger.info("KeyValue 저장 성공: {}", dto.key)
            return KeyValueDto.fromKeyValue(savedKeyValue)
        } catch (e: IllegalArgumentException) {
            logger.error("KeyValue 저장 실패: 유효하지 않은 데이터, 키: {}", dto.key, e)
            throw BusinessException(ErrorCode.INVALID_INPUT)
        }
    }

    fun getKeyValue(key: String): KeyValueDto? {
        return repository.findByKey(key)?.let(KeyValueDto::fromKeyValue)
            ?: run {
                logger.warn("KeyValue 조회 실패: 키 {}에 해당하는 값이 존재하지 않음", key)
                throw BusinessException(ErrorCode.KEY_NOT_FOUND)
            }
    }

    fun deleteKeyValue(key: String): Boolean {
        val result = repository.deleteByKey(key)
        if (!result) {
            logger.warn("KeyValue 삭제 실패: 키 {}에 해당하는 값이 존재하지 않음", key)
            throw BusinessException(ErrorCode.KEY_NOT_FOUND)
        }
        logger.info("KeyValue 삭제 성공: 키 {}", key)
        return result
    }
}
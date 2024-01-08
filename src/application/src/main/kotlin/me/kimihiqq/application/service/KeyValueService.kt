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
            logger.info("KeyValue 저장 성공: 키 - {}, 값 - {}", dto.key, dto.value)
            return KeyValueDto.fromKeyValue(savedKeyValue)
        } catch (e: IllegalArgumentException) {
            logger.error("KeyValue 저장 실패: 유효하지 않은 데이터, 키: {}", dto.key, e)
            throw BusinessException(ErrorCode.INVALID_INPUT)
        } catch (e: Exception) {
            logger.error("KeyValue 저장 실패: 데이터베이스 오류, 키: {}", dto.key, e)
            throw BusinessException(ErrorCode.DATABASE_ERROR)
        }
    }

    fun getKeyValue(key: String): KeyValueDto {
        return try {
            repository.findByKey(key)?.let {
                logger.info("KeyValue 조회 성공: 키 - {}, 값 - {}", key, it.value)
                KeyValueDto.fromKeyValue(it)
            } ?: throw BusinessException(ErrorCode.KEY_NOT_FOUND)
        } catch (e: BusinessException) {
            throw e
        } catch (e: Exception) {
            logger.error("KeyValue 조회 실패: 데이터베이스 오류, 키: {}", key, e)
            throw BusinessException(ErrorCode.DATABASE_ERROR)
        }
    }

    fun deleteKeyValue(key: String): Boolean {
        try {
            val result = repository.deleteByKey(key)
            if (!result) {
                throw BusinessException(ErrorCode.KEY_NOT_FOUND)
            }
            logger.info("KeyValue 삭제 성공: 키 {}", key)
            return true
        } catch (e: BusinessException) {
            throw e
        } catch (e: Exception) {
            logger.error("KeyValue 삭제 실패: 데이터베이스 오류, 키: {}", key, e)
            throw BusinessException(ErrorCode.DATABASE_ERROR)
        }
    }
}

package me.kimihiqq.presentation.serviceImpl

import io.grpc.Status
import io.grpc.stub.StreamObserver
import me.kimihiqq.application.dto.KeyValueDto
import me.kimihiqq.application.service.KeyValueService
import me.kimihiqq.domain.error.ErrorCode
import me.kimihiqq.domain.error.exception.BusinessException
import me.kimihiqq.grpc.*


class KeyValueServiceImpl(
    private val keyValueService: KeyValueService
) : KeyValueServiceGrpc.KeyValueServiceImplBase() {

    override fun get(request: GetRequest, responseObserver: StreamObserver<GetResponse>) {
        try {
            val keyValueDto = keyValueService.getKeyValue(request.key)
            val response = GetResponse.newBuilder()
                .setKey(request.key)
                .setValue(keyValueDto.value ?: "")
                .build()
            responseObserver.onNext(response)
        } catch (e: BusinessException) {
            val status = when (e.errorCode) {
                ErrorCode.KEY_NOT_FOUND -> Status.NOT_FOUND
                ErrorCode.INVALID_INPUT -> Status.INVALID_ARGUMENT
                ErrorCode.DATABASE_ERROR -> Status.INTERNAL
                else -> Status.UNKNOWN
            }
            responseObserver.onError(status.withDescription(e.errorCode.message).asRuntimeException())
        } finally {
            responseObserver.onCompleted()
        }
    }

    override fun save(request: SaveRequest, responseObserver: StreamObserver<SaveResponse>) {
        try {
            val keyValueDto = KeyValueDto(request.key, request.value)
            val savedKeyValue = keyValueService.saveKeyValue(keyValueDto)
            val response = SaveResponse.newBuilder()
                .setKey(savedKeyValue.key)
                .setValue(savedKeyValue.value ?: "")
                .build()
            responseObserver.onNext(response)
        } catch (e: BusinessException) {
            val status = when (e.errorCode) {
                ErrorCode.KEY_NOT_FOUND -> Status.NOT_FOUND
                ErrorCode.INVALID_INPUT -> Status.INVALID_ARGUMENT
                ErrorCode.DATABASE_ERROR -> Status.INTERNAL
                else -> Status.UNKNOWN
            }
            responseObserver.onError(status.withDescription(e.errorCode.message).asRuntimeException())
        } finally {
            responseObserver.onCompleted()
        }
    }

    override fun delete(request: DeleteRequest, responseObserver: StreamObserver<DeleteResponse>) {
        try {
            keyValueService.deleteKeyValue(request.key)
            val response = DeleteResponse.newBuilder()
                .setKey(request.key)
                .setSuccess(true)
                .build()
            responseObserver.onNext(response)
        } catch (e: BusinessException) {
            val status = when (e.errorCode) {
                ErrorCode.KEY_NOT_FOUND -> Status.NOT_FOUND
                ErrorCode.INVALID_INPUT -> Status.INVALID_ARGUMENT
                ErrorCode.DATABASE_ERROR -> Status.INTERNAL
                else -> Status.UNKNOWN
            }
            responseObserver.onError(status.withDescription(e.errorCode.message).asRuntimeException())
        } finally {
            responseObserver.onCompleted()
        }
    }
}
